package com.juaracoding.pcmspringboot29.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

public class CustomerDTO {
    @Pattern(regexp = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$",message = "Nama lengkap hanya boleh mengandung huruf, spasi, dan karakter tanda baca standar (seperti tanda hubung, titik, atau tanda petik tunggal). Pastikan nama tidak dimulai dengan karakter spesial atau angka.")
    private String nama;
    @Pattern(regexp = "^(?=.{1,256})(?=.{1,64}@.{1,255}$)(?:(?![.])[a-zA-Z0-9._%+-]+(?:(?<!\\\\)[.][a-zA-Z0-9-]+)*?)@[a-zA-Z0-9.-]+(?:\\.[a-zA-Z]{2,})+$",message = "wajib format email")
    private String email;
    @Pattern(regexp = "^[a-zA-Z0-9._-]{3,16}$",message = "nama alfanumerik min 3 max 16")
    private String username;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("tanggal_lahir")
    private String tanggalLahir;
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[@_#\\-$])[\\w].{8,15}$",
            message = "Format minimal 1 angka, 1 huruf kecil, 1 huruf besar, 1 spesial karakter (_ \"Underscore\", - \"Hyphen\", # \"Hash\", atau $ \"Dollar\" atau @ \"At\") setelah 4 kondisi min 9 max 16 alfanumerik, contoh : aB4$12345")
    private String password;

    @Pattern(regexp = "^(62|\\+62|0)8[0-9]{9,13}$",
            message = "Format No HP Tidak Valid , min 9 max 13 setelah angka 8, contoh : (0/62/+62)81111111")
    @JsonProperty("no_hp")
    private String noHp;

    private String tambahan;

    public String getTambahan() {
        return tambahan;
    }

    public void setTambahan(String tambahan) {
        this.tambahan = tambahan;
    }

    public @Pattern(regexp = "^(62|\\+62|0)8[0-9]{9,13}$",
            message = "Format No HP Tidak Valid , min 9 max 13 setelah angka 8, contoh : (0/62/+62)81111111") String getNoHp() {
        return noHp;
    }

    public void setNoHp(@Pattern(regexp = "^(62|\\+62|0)8[0-9]{9,13}$",
            message = "Format No HP Tidak Valid , min 9 max 13 setelah angka 8, contoh : (0/62/+62)81111111") String noHp) {
        this.noHp = noHp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public @Pattern(regexp = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$", message = "Nama lengkap hanya boleh mengandung huruf, spasi, dan karakter tanda baca standar (seperti tanda hubung, titik, atau tanda petik tunggal). Pastikan nama tidak dimulai dengan karakter spesial atau angka.") String getNama() {
        return nama;
    }

    public void setNama(@Pattern(regexp = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$", message = "Nama lengkap hanya boleh mengandung huruf, spasi, dan karakter tanda baca standar (seperti tanda hubung, titik, atau tanda petik tunggal). Pastikan nama tidak dimulai dengan karakter spesial atau angka.") String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email) {
        this.email = email;
    }

    public @Pattern(regexp = "^[a-zA-Z0-9._-]{3,16}$", message = "nama alfanumerik min 3 max 16") String getUsername() {
        return username;
    }

    public void setUsername(@Pattern(regexp = "^[a-zA-Z0-9._-]{3,16}$", message = "nama alfanumerik min 3 max 16") String username) {
        this.username = username;
    }

    public String getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(String tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }
}
