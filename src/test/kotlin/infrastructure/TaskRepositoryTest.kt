package infrastructure

import com.example.infrastructure.TaskRepository
import domain.createTaskFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TaskRepositoryTest {

    @Test
    fun deprecatedBulkRegister() {
        val tasks = List(3) {
            createTaskFixture()
        }

        assertThat(TaskRepository.deprecatedBulkRegister(tasks)).isEqualTo(tasks.size)
    }

    @Test
    fun bulkRegister() {
        val tasks = List(3) {
            createTaskFixture()
        }

        assertThat(TaskRepository.bulkRegister(tasks)).isEqualTo(tasks.size)
    }
}
