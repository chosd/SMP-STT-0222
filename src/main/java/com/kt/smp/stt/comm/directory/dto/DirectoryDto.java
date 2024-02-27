package com.kt.smp.stt.comm.directory.dto;

import lombok.Getter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Getter
public class DirectoryDto {

    private int id;
    private String name;
    private String homePath;
    private String path;
    private int priority;
    private String inUse;

    public Path getAbsolutePath() {
        return Paths.get(homePath, path);
    }
    public Path getAbsolutePath2() {
        return Paths.get(path);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DirectoryDto that = (DirectoryDto) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
