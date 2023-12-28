import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

val BRICK_A = Brick(Point(1, 0, 1), Point(1, 2, 1), 1)
val BRICK_B = Brick(Point(0, 0, 2), Point(2, 0, 2), 1)
val BRICK_F = Brick(Point(0, 1, 6), Point(2, 1, 6), 1)
val BRICK_G = Brick(Point(1, 1, 7), Point(1, 1, 8), 1)

internal class Day22Test {
    @Test
    fun should_return_correct_for_x_direction_brick() {
        val actual = generateAllPositionsToCheck(Brick(Point(0, 0, 0), Point(2, 0, 0), 1))
        
        assertEquals(listOf(Pos(0, 0), Pos(1, 0), Pos(2, 0)), actual)
    }
    
    @Test
    fun should_return_correct_for_y_direction_brick() {
        val actual = generateAllPositionsToCheck(Brick(Point(0, 0, 0), Point(0, 1, 0), 1))

        assertEquals(listOf(Pos(0, 0), Pos(0, 1)), actual)
    }
    
    @Test
    fun should_return_correct_for_z_direction_brick() {
        val actual = generateAllPositionsToCheck(Brick(Point(2, 2, 0), Point(2, 2, 5), 1))

        assertEquals(listOf(Pos(2, 2)), actual)
    }
    
    @Test
    fun should_work_when_brick_is_listed_in_reverse_order() {
        val actual = generateAllPositionsToCheck(Brick(Point(2, 0, 0), Point(0, 0, 0), 1))

        assertEquals(listOf(Pos(0, 0), Pos(1, 0), Pos(2, 0)), actual)
    }
    
    @Test
    fun brick_b_should_rest_on_brick_a() {
        assertTrue(restsOn(BRICK_B, BRICK_A))
    }
    
    @Test
    fun brick_a_should_not_rest_on_brick_b() {
        assertFalse(restsOn(BRICK_A, BRICK_B))
    }
    
    @Test
    fun brick_g_should_rest_of_brick_f() {
        assertTrue(restsOn(BRICK_G, BRICK_F))
    }
}