package bg.sofia.uni.fmi.mjt.todoist.server.functionality.type;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Task {
    private String name;
    private LocalDateTime date;
    private LocalDateTime dueDate;
    private String description;
    private boolean completed;
    private String asignee;

    public Task(String name, LocalDateTime date, LocalDateTime dueDate, String description) {
        this.name = name;
        this.date = date;
        this.dueDate = dueDate;
        this.description = description;
        this.completed = false;
    }

    public String name() {
        return name;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public void updateDueDate(LocalDateTime dueDate) {
        if (dueDate == null) {
            return;
        }
        this.dueDate = dueDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void updateDescription(String description) {
        if (description == null) {
            return;
        }
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void complete() {
        completed = true;
    }

    public void setAsignee(String asignee) {
        this.asignee = asignee;
    }

    public String getAsignee() {
        return asignee;
    }

    @Override
    public String toString() {
        String message = "";
        List<String> parameters = new ArrayList<>();
        parameters.add(name);
        if (date == null) {
            parameters.add(null);
        } else {
            parameters.add(date.toString());
        }
        if (dueDate == null) {
            parameters.add(null);
        } else {
            parameters.add(dueDate.toString());
        }
        parameters.add(description);

        int parametersLength = parameters.size();
        for (int i = 0; i < parametersLength; i++) {
            if (parameters.get(i) == null) {
                continue;
            }
            if (i == 0) {
                message += "name: ";
            } else if (i == 1) {
                message += " date: ";
            } else if (i == 2) {
                message += " due date: ";
            } else if (i == 3) {
                message += System.lineSeparator() + "description: ";
            }
            message += parameters.get(i);
        }

        return message + System.lineSeparator();
    }

    @Override
    public int hashCode() {
        return 31 * name.hashCode(); // + date.hashCode() + dueDate.hashCode() + description.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Task)) {
            return false;
        }

        Task task = (Task) obj;
        return name.equals(task.name);
    }
}
