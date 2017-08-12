package xyz.notarealtree.airmart.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import xyz.notarealtree.airmart.AirMartConfiguration
import xyz.notarealtree.airmart.model.Doctrine
import xyz.notarealtree.airmart.model.DoctrineConfig
import xyz.notarealtree.airmart.model.Fitting
import java.io.File
import java.nio.file.*
import java.util.stream.Collectors

class FittingManager(val configuration: AirMartConfiguration) {
    val mapper: ObjectMapper= ObjectMapper(YAMLFactory())
    val regex = Regex("(x\\s?[0-9].*|[0-9].*\\s?x)")

    init {
        mapper.registerModule(KotlinModule()) // Enable Kotlin support
    }

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
                .map({ file -> parseDoctrine(file) })
                .collect(Collectors.toList())
    }

    fun parseDoctrine(file: File?): Doctrine {
        if (file == null) {
            throw IllegalArgumentException("Somehow file passed in by file tree walking is null")
        }
        val doctrineFile = File("${file.path}/doctrine.yml")
        val text = doctrineFile.readText()
        val doctrineConfig = mapper.readValue<DoctrineConfig>(text)

        val fittings = file.listFiles().
                        filter({subFile -> subFile.name.endsWith(".fit")}).
                        stream().map({fittingFile -> parseFitting(fittingFile)}).collect(Collectors.toList())
        return Doctrine(doctrineConfig.name, doctrineConfig.type, doctrineConfig.colourClass, fittings)
    }

    fun parseFitting(file: File): Fitting {
        val name = file.name.replace(".fit", "")
        val lines = file.readLines()
        val type = lines[0]
        val items = mutableMapOf<String, Int>()
        lines.filter({line -> line != ""})
                .forEach({line ->
                    run {
                        items.computeIfPresent(line, { _, v -> 1 + v })
                        items.putIfAbsent(line, 1)
                    }
                })

        val outputLines = items.keys.map { key ->
            if (regex.containsMatchIn(key)) {
                key
            } else {
                "$key x${items[key]}"
            }
        }
        return Fitting(name, type, outputLines)
    }


}