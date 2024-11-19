package org.example.desafiotools.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.desafiotools.dto.DetalhamentoTransacaoDTO;
import org.example.desafiotools.enums.StatusFormaPagamento;
import org.example.desafiotools.enums.StatusTransacao;
import org.example.desafiotools.util.Constants;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity(name = "transacao")
@Table(schema = Constants.SCHEMA_DESAFIO)
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

    @Transient
    private String tipoPagamentoString;

    @Column
    @Enumerated(EnumType.STRING)
    private StatusTransacao status;

    @Column
    private String codigoAutorizacao;

    @Column
    private String nsu;

    @Column
    private Integer parcelas;

    public Transacao () {

    }

    public Transacao (DetalhamentoTransacaoDTO detalhamentoTransacaoDto) {
        this.cartao = detalhamentoTransacaoDto.getCartao();
        this.valor = detalhamentoTransacaoDto.getDescricao().getValor();
        this.estabelecimento = detalhamentoTransacaoDto.getDescricao().getEstabelecimento();
        this.codigoAutorizacao = detalhamentoTransacaoDto.getDescricao().getCodigoAutorizacao();
        this.nsu = detalhamentoTransacaoDto.getDescricao().getNsu();
        this.parcelas = Objects.nonNull(detalhamentoTransacaoDto.getFormaPagamento().getParcelas()) ? detalhamentoTransacaoDto.getFormaPagamento().getParcelas() : 1;
    }

    public Transacao (DetalhamentoTransacaoDTO detalhamentoTransacaoDto, String status) {
        this.cartao = detalhamentoTransacaoDto.getCartao();
        this.valor = detalhamentoTransacaoDto.getDescricao().getValor();
        this.estabelecimento = detalhamentoTransacaoDto.getDescricao().getEstabelecimento();
        this.codigoAutorizacao = detalhamentoTransacaoDto.getDescricao().getCodigoAutorizacao();
        this.nsu = detalhamentoTransacaoDto.getDescricao().getNsu();
        this.parcelas = Objects.nonNull(detalhamentoTransacaoDto.getFormaPagamento().getParcelas()) ? detalhamentoTransacaoDto.getFormaPagamento().getParcelas() : 1;
        this.status = StatusTransacao.valueOf(status);
        this.dataHora = LocalDateTime.now();
        this.tipoPagamento = StatusFormaPagamento.valueOf(detalhamentoTransacaoDto.getFormaPagamento().getTipo());
    }

}
