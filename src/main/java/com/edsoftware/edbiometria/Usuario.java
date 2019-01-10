package com.edsoftware.edbiometria;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(indexes = { @Index(name = "IDX_FINGERPRINT", columnList = "fingerprint") })
public class Usuario {

    @Id
    @GeneratedValue
    private Integer id;
    @Column(length=16777215)
    private byte[] fingerPrint;
}