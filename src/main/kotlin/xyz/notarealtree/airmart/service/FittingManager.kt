package xyz.notarealtree.airmart.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import xyz.notarealtree.airmart.AirMartConfiguration
import xyz.notarealtree.airmart.model.Doctrine
import xyz.notarealtree.airmart.model.Fitting
import java.io.File
import java.nio.file.*
import java.util.stream.Collectors

class FittingManager(val configuration: AirMartConfiguration) {

    val watcher = FileSystems.getDefault().newWatchService()
    val watchKey = Paths.get("").register(watcher,
            StandardWatchEventKinds.ENTRY_CREATE,
            StandardWatchEventKinds.ENTRY_DELETE,
            StandardWatchEventKinds.ENTRY_MODIFY)

    var doctrines: List<Doctrine> = listOf()

    fun getFittings(): List<Doctrine> {
        walkFiles()
        return doctrines
    }

    fun walkFiles() {
        val rootDir = File(configuration.fittingsBasePath)
        val files = rootDir.listFiles()
        doctrines = files.filter({ file -> file.isDirectory })
                .stream()
                .map({ file -> parseDoctrine(file) }).collect(Collectors.toList())
    }

    fun parseDoctrine(file: File?): Doctrine {
        val name = if (file == null) "unnamed" else file.name
        val fittings = if (file == null) listOf<Fitting>() else
                file.listFiles().
                        filter({subFile -> subFile.name.endsWith(".fit")}).
                        stream().map({fittingFile -> parseFitting(fittingFile)}).collect(Collectors.toList())
        return Doctrine(name, fittings)
    }

    fun parseFitting(file: File): Fitting {
        val name = file.name.replace(".fit", "")
        val lines = file.readLines()
        val type = lines[0]
        val items = mutableMapOf<String, Int>()
        lines.subList(1, lines.size)
                .filter({line -> line != ""})
                .forEach({line ->
                    run {
                        items.computeIfPresent(line, { _, v -> 1 + v })
                        items.putIfAbsent(line, 1)
                    }
                })

        val outputLines = items.keys.map { key -> "$key x${items[key]}" }
        return Fitting(name, type, outputLines)
    }


}