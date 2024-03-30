import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.documentfile.provider.DocumentFile

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let { ctx ->
            playRandomSound(ctx)
        }
    }

    private fun playRandomSound(context: Context) {
        val sharedPrefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val soundFolderUriString = sharedPrefs.getString("soundFolderUri", null)
        if (soundFolderUriString != null) {
            val soundFolderUri = Uri.parse(soundFolderUriString)
            val soundFolder = DocumentFile.fromTreeUri(context, soundFolderUri)
            val sounds = soundFolder?.listFiles()?.filter { it.isFile && it.canRead() }

            if (sounds != null && sounds.isNotEmpty()) {
                val randomSound = sounds[Random().nextInt(sounds.size)]
                val mediaPlayer = MediaPlayer().apply {
                    setDataSource(context, randomSound.uri)
                    prepare()
                }
                mediaPlayer.start()
            }
        }
    }
}
