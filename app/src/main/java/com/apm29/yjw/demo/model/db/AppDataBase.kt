
import androidx.room.Database
import androidx.room.RoomDatabase
import com.apm29.yjw.demo.model.db.dao.PushMessageDao
import com.apm29.yjw.demo.model.db.entity.PushMessage

@Database(entities = [PushMessage::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pushMessageDao(): PushMessageDao
}