package ad.blocktest.util

import java.util.Random

fun IntRange.random() = Random().nextInt((endInclusive + 1) - start) + start
