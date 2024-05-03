package com.carbonwatch.carbonja.batch.common;

import com.carbonwatch.carbonja.batch.dto.dtoMetric.RootMetric;
import com.carbonwatch.carbonja.batch.dto.dtoProperties.RootProperties;
import com.carbonwatch.carbonja.domain.file.FileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ItemWriter {

    @Autowired
    FileRepository fileRepository;

    public void writeMetric(String jsonPathName, RootMetric rootMetric) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonPathNameClean = jsonPathName.replace(" ", "").replace(":", "");
        Path path = Paths.get(jsonPathNameClean);
        String contentTypeInput = Files.probeContentType(path);

        ///////////////////////////        A CONSERVER POUR UN USAGE DANS REPERTOIRE LOCAL        //////////////////////
        Files.write(path, objectMapper.writeValueAsString(rootMetric).getBytes());    ///////////////////////////////
        ///////////////////////////        A CONSERVER POUR UN USAGE DANS REPERTOIRE LOCAL        //////////////////////
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(objectMapper.writeValueAsString(rootMetric).getBytes());
        long sizeInput = objectMapper.writeValueAsString(rootMetric).getBytes().length;
        fileRepository.saveFile(jsonPathNameClean, byteArrayInputStream, sizeInput, contentTypeInput);

    }

    public void writeProperties(String jsonPathName, List<RootProperties> rootPropertiesList ) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonPathNameClean = jsonPathName.replace(" ", "").replace(":", "");
        Path path = Paths.get(jsonPathNameClean);
        String contentTypeInput = Files.probeContentType(path);

        ///////////////////////////        A CONSERVER POUR UN USAGE DANS REPERTOIRE LOCAL        //////////////////////
        Files.write(path, objectMapper.writeValueAsString(rootPropertiesList).getBytes()); //////////////////////////
        ///////////////////////////        A CONSERVER POUR UN USAGE DANS REPERTOIRE LOCAL        //////////////////////
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(objectMapper.writeValueAsString(rootPropertiesList).getBytes());
        long sizeInput = objectMapper.writeValueAsString(rootPropertiesList).getBytes().length;
        fileRepository.saveFile(jsonPathNameClean, byteArrayInputStream, sizeInput, contentTypeInput);

    }
}
