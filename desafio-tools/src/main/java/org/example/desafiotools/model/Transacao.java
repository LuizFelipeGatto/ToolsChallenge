package org.example.desafiotools.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.desafiotools.enums.StatusFormaPagamento;
import org.example.desafiotools.enums.StatusTransacao;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "transacao")
@Table(schema = "desafio")
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private String cartao;

    @Column
    private Double valor;

    @Column
    private LocalDateTime dataHora;

    @Column
    private String estabelecimento;

    @Column
    @Enumerated(EnumType.STRING)
    private StatusFormaPagamento tipoPagamento;

    @Column
    @Enumerated(EnumType.STRING)
    private StatusTransacao status;

    @Column
    private String codigoAutorizacao;

    @Column
    private String nsu;

    @Column
    private Integer parcelas;

}
