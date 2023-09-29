package com.ekezet.othello.core.data.models

typealias Board = Array<Array<Disk?>>

const val BoardWidth = 8
const val BoardHeight = 8

fun Board.putAtAndClone(x: Int, y: Int, disk: Disk): Board =
    clone().apply {
        this[y] = this[y].clone().apply {
            this[x] = disk
        }
    }

fun Board.putAtAndClone(position: Position, disk: Disk): Board =
    putAtAndClone(position.x, position.y, disk)

fun Board.putAt(position: Position, disk: Disk) {
    this[position.y][position.x] = disk
}

fun Board.getAt(position: Position): Disk? =
    this[position.y][position.x]
