package ru.petrelevich.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MagicController {
    public static final long MAGIC_ROOM = 1408;

    @GetMapping(value = "/api/{roomId}/ismagic", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean getIsMagic(@PathVariable String roomId) {
        return String.valueOf(MAGIC_ROOM).equals(roomId);
    }
}
