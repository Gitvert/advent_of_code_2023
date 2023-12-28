import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day24KtTest {
    @Test
    fun parallel_lines_should_return_null() {
        assertNull(findIntersection(
            HailStone(BigPoint(18, 19, 22), BigPoint(-1, -1, -2)),
            HailStone(BigPoint(20, 25, 34), BigPoint(-2, -2, -4))
        ))
    }
    
    @Test
    fun should_intersect() {
        assertNotNull(findIntersection(
            HailStone(BigPoint(19, 13, 30), BigPoint(-2, 1, -2)),
            HailStone(BigPoint(18, 19, 22), BigPoint(-1, -1, -2))
        ))
    }
    
    @Test
    fun should_intersect_at_correct_position() {
        val actual = findIntersection(
            HailStone(BigPoint(12, 31, 28), BigPoint(-1, -2, -1)),
            HailStone(BigPoint(18, 19, 22), BigPoint(-1, -1, -2))
        )
        
        assertEquals(Intersection(-6.0, -5.0), actual)
    }
}