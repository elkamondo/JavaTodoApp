package io.github.elkamondo.utils.reports;

import io.github.elkamondo.exceptions.FileNameNotValidException;
import io.github.elkamondo.models.Todo;

import java.io.IOException;
import java.util.Collection;

public interface TodoReporter {

    boolean save(Collection<? extends Todo> todos, String filename) throws IOException, FileNameNotValidException;

    Collection<? extends Todo> load(String filename) throws IOException, FileNameNotValidException;

}
