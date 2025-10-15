package model;

import java.util.Objects;

public class Task
{
    protected String name;
    protected String description;
    protected int id;
    protected Status status;

    public Task()
    {
    }

    public Task(Task otherTask)
    {
        this.id = otherTask.id;
        this.name = otherTask.name;
        this.description = otherTask.description;
        this.status = otherTask.status;

    }

    @Override
    public String toString()
    {
        return "\nTask{ " +
                "name= '" + name + '\'' +
                ", description= '" + description + '\'' +
                ", id= " + id +
                ", status= " + status +
                "}";
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null || getClass() != o.getClass())
            return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description)
                && status == task.status;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, description, id, status);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public Status getStatus()
    {
        return status;
    }

    public void setStatus(Status status)
    {
        this.status = status;
    }
}


