package xyz.notarealtree.airmart.service

import java.nio.file.*

class FittingManager {

    val watcher = FileSystems.getDefault().newWatchService()
    val watchKey = Paths.get("").register(watcher,
            StandardWatchEventKinds.ENTRY_CREATE,
            StandardWatchEventKinds.ENTRY_DELETE,
            StandardWatchEventKinds.ENTRY_MODIFY)
}