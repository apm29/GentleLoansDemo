package com.apm29.yjw.demo.di.module

import android.app.Application
import android.os.Build
import android.util.Log
import cn.jpush.android.api.JPushInterface
import com.apm29.yjw.demo.app.exception.UserInfoExpiredException
import com.apm29.yjw.demo.arch.UserManager
import com.apm29.yjw.demo.model.BaseBean
import com.apm29.yjw.gentleloansdemo.BuildConfig
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.github.simonpercic.oklog3.OkLogInterceptor
import com.google.gson.*
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.*
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class ConfigModule {
    @Singleton
    @Provides
    fun provideGsonConfig(): AppModule.GsonConfiguration {
        return object : AppModule.GsonConfiguration {
            override fun configGson(application: Application, builder: GsonBuilder) {
                builder
                        .serializeNulls()
                        .enableComplexMapKeySerialization()
                        .registerTypeAdapter(Boolean::class.java, typeAdapter)

            }
        }
    }


    @Singleton
    @Provides
    fun providesRetrofitConfig(gson: Gson, okHttpClient: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
    }

    @Singleton
    @Provides
    fun providesOkHttpClientConfig(gson: Gson, application: Application): OkHttpClient {
        return OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .addInterceptor {
                    val oldRequest = it.request()
                    var newRequest = oldRequest

                    if (!BuildConfig.BASE_URL.contains(oldRequest.url().host(), true)
                            || oldRequest.url().url().path.contains("/upload/test", true)
                    ) {
                        return@addInterceptor it.proceed(oldRequest)
                    }

                    val token = UserManager.currentUser?.accessToken ?: ""
                    val registrationID = JPushInterface.getRegistrationID(application)
                    val versionCode = BuildConfig.VERSION_CODE
                    //添加公共参数

                    if ("POST".equals(oldRequest.method(), true)) {
                        val body = oldRequest.body()
                        val formBody: FormBody
                        formBody = body as? FormBody ?: FormBody.Builder().build()

                        val bizBuilder = getJSONStringBuilder(formBody)

                        //构建JSon
                        val jsonBuilder = StringBuilder()
                        jsonBuilder.append("{")
                        jsonBuilder.append("\"").append("access_token").append("\"").append(":").append("\"").append(token).append("\"").append(",")
                        jsonBuilder.append("\"").append("registration_id").append("\"").append(":").append("\"").append(registrationID).append("\"").append(",")
                        jsonBuilder.append("\"").append("_app_type").append("\"").append(":").append("\"").append("android").append("\"").append(",")
                        jsonBuilder.append("\"").append("is_simulator").append("\"").append(":").append("\"").append((Build.SERIAL == "unknown")).append("\"").append(",")
                        jsonBuilder.append("\"").append("version_code").append("\"").append(":").append("\"").append(versionCode).append("\"").append(",")

                        jsonBuilder.append("\"").append("biz_content").append("\"").append(":").append(bizBuilder.toString())
                        jsonBuilder.append("}")

                        printRequestBody(jsonBuilder.toString())

                        val requestBody = FormBody.create(MediaType.parse("Content-Type:application/json; charset=utf-8"), jsonBuilder.toString())
                        newRequest = newRequest.newBuilder()
                                .addHeader("content-type", "application/json; charset=utf-8")
                                .post(requestBody)
                                .build()
                    }
                    return@addInterceptor it.proceed(newRequest)
                }
                .addInterceptor(
                        //日志LOG
                        LoggingInterceptor.Builder()
                                .loggable(BuildConfig.DEBUG)
                                .setLevel(Level.BASIC)
                                .log(Platform.INFO)
                                .request("Request-LOG")
                                .response("Response-LOG")
                                .addHeader("version", BuildConfig.VERSION_NAME)
                                .build()
                )
                .addInterceptor(
                        OkLogInterceptor.builder()
                                .withProtocol(true)
                                .withResponseHeaders(true)
                                .shortenInfoUrl(true)
                                .withRequestContentType(true)
                                .build()
                )
                .addNetworkInterceptor(StethoInterceptor())
                .addNetworkInterceptor {
                    val response: Response?
                    try {
                        response = it.proceed(it.request())
                    } catch (e: Exception) {
                        Log.w("ConfigModule", "Http Error: $e")
                        throw e
                    }

                    /**
                     * ResponseBody.string()方法调用后会关闭流,需要重新建立response
                     */
                    val jsonString = response.body()?.string()
                    val mediaType = response.body()?.contentType()
                    try {
                        val baseBean = gson.fromJson<BaseBean<String>>(jsonString, BaseBean::class.java)
                        if (baseBean.code == 401) {
                            throw UserInfoExpiredException(baseBean.msg)
                        }
                    } catch (u: UserInfoExpiredException) {
                        Log.w("ConfigModule", "user info isDataExpired ,redirect to login page")
                        throw u
                    } catch (e: Exception) {

                    }
                    return@addNetworkInterceptor response.newBuilder().body(ResponseBody.create(mediaType, jsonString
                            ?: "")).build()
                }
                .build()
    }

    val prettyGson = GsonBuilder().setPrettyPrinting().create()
    val jp = JsonParser()
    private fun printRequestBody(json: String) {
        val jsonElement = jp.parse(json)
        println(prettyGson.toJson(jsonElement))
    }


    private fun getJSONStringBuilder(formBody: FormBody): StringBuilder {
        val bizBuilder = StringBuilder("{")
        for (i in 0 until formBody.size()) {
            val name = formBody.name(i)
            val value = formBody.value(i)
            bizBuilder
                    .append("\"").append(name).append("\"")
                    .append(":")
                    .append("\"").append(value).append("\"")
                    .append(",")
            if (i == formBody.size() - 1) {
                bizBuilder.deleteCharAt(bizBuilder.lastIndexOf(","))
            }
        }
        bizBuilder.append("}")
        //只有一个biz_content的时候直接返回原String
        return if (formBody.size() == 1 && formBody.name(0).equals("biz_content", ignoreCase = true)) {
            StringBuilder(formBody.value(0))
        } else bizBuilder
    }


    @Singleton
    @Provides
    fun providesRetrofit(retrofitConfig: Retrofit.Builder): Retrofit {
        return retrofitConfig.build()
    }

    val typeAdapter = object : TypeAdapter<Boolean>() {
        override fun write(out: JsonWriter, value: Boolean?) {
            if (value == null) {
                out.nullValue()
            } else {
                out.value(value)
            }
        }

        override fun read(inReader: JsonReader): Boolean? {
            val peek = inReader.peek()
            return when (peek) {
                JsonToken.BOOLEAN -> inReader.nextBoolean()
                JsonToken.NULL -> {
                    inReader.nextNull()
                    null
                }
                JsonToken.NUMBER -> inReader.nextInt() != 0
                JsonToken.STRING -> java.lang.Boolean.parseBoolean(inReader.nextString())
                else -> throw IllegalStateException("Expected BOOLEAN or NUMBER but was $peek")
            }
        }
    }
}