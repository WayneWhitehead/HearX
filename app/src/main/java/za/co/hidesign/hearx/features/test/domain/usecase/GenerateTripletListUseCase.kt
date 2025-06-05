package za.co.hidesign.hearx.features.test.domain.usecase

import javax.inject.Inject
import kotlin.random.Random

class GenerateTripletListUseCase @Inject constructor() {
    operator fun invoke(count: Int = 10): List<List<Int>> {
        val triplets = mutableListOf<List<Int>>()
        val generated = mutableSetOf<List<Int>>()
        var prev: List<Int> = emptyList()

        while (triplets.size < count) {
            val candidate = List(3) { Random.nextInt(1, 10) }
            val isUnique = candidate !in generated
            val noRepeatInPosition = prev.isEmpty() || candidate.zip(prev).all { it.first != it.second }
            if (isUnique && noRepeatInPosition) {
                triplets.add(candidate)
                generated.add(candidate)
                prev = candidate
            }
        }
        return triplets
    }
}