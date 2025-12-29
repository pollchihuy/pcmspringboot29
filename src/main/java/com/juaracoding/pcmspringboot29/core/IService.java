package com.juaracoding.pcmspringboot29.core;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IService<T> {
    /** CRUD */
    //AUT02FE034
    public ResponseEntity<Object> save(T t, HttpServletRequest request);//001-010
    public ResponseEntity<Object> update(Long id, T t, HttpServletRequest request);//011-020
    public ResponseEntity<Object> delete(Long id, HttpServletRequest request);//021-030
    public ResponseEntity<Object> findAll(Pageable pageable, HttpServletRequest request);//031-040
    public ResponseEntity<Object> findById(Long id, HttpServletRequest request);//041-050
    public ResponseEntity<Object> findByParam(Pageable pageable,String columnName,String value, HttpServletRequest request);//051-060
    public ResponseEntity<Object> save(T t,MultipartFile file, HttpServletRequest request);//061-070
    public ResponseEntity<Object> update(Long id, T t,MultipartFile file, HttpServletRequest request);//071-080
//    public ResponseEntity<Object> findByParam(Pageable pageable, String columnName, String value, LocalDate from,LocalDate to, HttpServletRequest request);//101-110
}