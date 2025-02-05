package infrastructure

import com.example.domain.Priority
import com.example.domain.Task
import com.example.infrastructure.TaskRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*

class TaskRepositoryTest {

    @Test
    fun deprecatedBulkRegister() {
        val tasks = listOf(
            Task("tenant", UUID.randomUUID(), "title1", Priority.LOW),
            Task("tenant", UUID.randomUUID(), "title2", Priority.HIGH),
            Task("tenant", UUID.randomUUID(), "title3", Priority.MEDIUM)
        )

        val counts = TaskRepository.deprecatedBulkRegister(tasks)

        assertThat(counts.size).isEqualTo(3)
    }

    @Test
    fun bulkRegister() {
        val tasks = listOf(
            Task("tenant", UUID.randomUUID(), "title1", Priority.LOW),
            Task("tenant", UUID.randomUUID(), "title2", Priority.HIGH),
            Task("tenant", UUID.randomUUID(), "title3", Priority.MEDIUM)
        )

        TaskRepository.bulkRegister(tasks)
    }
}
