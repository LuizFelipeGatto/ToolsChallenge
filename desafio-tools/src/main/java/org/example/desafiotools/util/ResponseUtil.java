package org.example.desafiotools.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseUtil {

    public static <T> ResponseEntity<T> processResponse(final T object) {
        return object != null ?
                ResponseEntity.ok(object) :
                new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public static <T> ResponseEntity<List<T>> processResponse(final List<T> lista) {
        return lista != null && !lista.isEmpty() ? ResponseEntity.ok(lista) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
