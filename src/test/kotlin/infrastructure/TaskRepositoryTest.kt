package infrastructure

import com.example.infrastructure.TaskRepository
import domain.createTask
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TaskRepositoryTest {

    @Test
    fun deprecatedBulkRegister() {
        val tasks = List(3) {
            createTask()
        }

        assertThat(TaskRepository.deprecatedBulkRegister(tasks)).isEqualTo(tasks.size)
    }

    @Test
    fun bulkRegister() {
        val tasks = List(3) {
            createTask()
        }

        assertThat(TaskRepository.bulkRegister(tasks)).isEqualTo(tasks.size)
    }
}
