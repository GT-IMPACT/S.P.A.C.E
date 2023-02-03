package space.impact.space.api.world.gen.other

enum class EnumCraterSize(val min: Int, val max: Int, private val prob: Int) {

    SMALL(8, 12, 14),
    MEDIUM(13, 17, 8),
    LARGE(18, 25, 2),
    EXTREME(26, 30, 1);

    companion object {
        var sizeArray: Array<EnumCraterSize?>

        init {
            var amount = 0
            val var1: Array<EnumCraterSize> = EnumCraterSize.values()
            val var2 = var1.size
            var var3: Int
            var3 = 0
            while (var3 < var2) {
                val c = var1[var3]
                amount += c.prob
                ++var3
            }
            sizeArray = arrayOfNulls(amount)
            var pointer = 0
            val var8: Array<EnumCraterSize> = EnumCraterSize.values()
            var3 = var8.size
            for (var9 in 0 until var3) {
                val c = var8[var9]
                for (i in 0 until c.prob) {
                    sizeArray[pointer] = c
                    ++pointer
                }
            }
        }
    }
}