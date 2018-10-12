package com.apm29.yjw.demo.ui.form

import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.apm29.yjw.demo.arch.BaseFragment
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.viewmodel.DefaultFragmentViewModel
import com.apm29.yjw.gentleloansdemo.R


/*
 * Defines an array that contains column names to move from
 * the Cursor to the ListView.
 */
private val PROJECTION = arrayOf(
        ContactsContract.Contacts._ID,
        ContactsContract.Contacts.LOOKUP_KEY,
        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
)


class FormListFragment : BaseFragment<DefaultFragmentViewModel>(), LoaderManager.LoaderCallbacks<Cursor> {
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        /*
     * Makes search string into pattern and
     * stores it in the selection array
     */
        // Starts the query
        return CursorLoader(
                requireContext(),
                ContactsContract.Contacts.CONTENT_URI,
                PROJECTION,
                null,
                null,
                null
        )
    }
    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        //mCursorAdapter.swapCursor(data)
        while (data?.moveToNext()==true){
           println("${data.getString(0)},${data.getString(1)},${data.getString(2)}")
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        //mCursorAdapter.swapCursor(null);
    }

    override fun setupViewLayout(savedInstanceState: Bundle?): Int {
        return R.layout.form_list_fragment
    }

    override fun setupModel(appComponent: AppComponent) {
        DaggerDefaultFragmentComponent.builder()
                .defaultFragmentModule(DefaultFragmentModule(this))
                .appComponent(appComponent)
                .build()
                .inject(this)
    }

    override fun setupViews(savedInstanceState: Bundle?) {

        //list.adapter = mCursorAdapter
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        // Initializes the loader
        LoaderManager.getInstance(requireActivity()).initLoader(1999, null, this)
    }
}