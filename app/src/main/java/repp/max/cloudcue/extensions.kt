package repp.max.cloudcue

fun <T : Any> List<T>.takeEach(xth: Int): List<T> = filterIndexed { i, _ -> i % xth == 0}