package com.github.sekkycodes.cucumber;

import com.github.sekkycodes.FileSystem;
import com.github.sekkycodes.dtos.TestCase;
import io.cucumber.gherkin.GherkinDocumentBuilder;
import io.cucumber.gherkin.Parser;
import io.cucumber.gherkin.TokenMatcher;
import io.cucumber.messages.IdGenerator;
import io.cucumber.messages.IdGenerator.UUID;
import io.cucumber.messages.types.GherkinDocument;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class FileInterpreter {

  private final FileSystem fileSystem;

  public FileInterpreter(FileSystem fileSystem) {
    this.fileSystem = Objects.requireNonNull(fileSystem);
  }

  public List<TestCase> parse(Path path) throws IOException {
    TokenMatcher matcher = new TokenMatcher("en");
    IdGenerator idGenerator = new UUID();
    Parser<GherkinDocument> parser = new Parser<>(new GherkinDocumentBuilder(idGenerator));
    String fileContent = fileSystem.read(path);
    GherkinDocument doc = parser.parse(fileContent, matcher);

    return convert(doc);
  }

  private List<TestCase> convert(GherkinDocument doc) {
    return new Converter().convert(doc);
  }
}
