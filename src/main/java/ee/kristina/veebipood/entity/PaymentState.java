package ee.kristina.veebipood.entity;

public enum PaymentState {
    initial, // maksmata
    settled, // edukas makse
    abandoned, // 15 min oodatud aga makse ei te
    failed, // tehnilised errorid voi raha otsas
    voided // kasutaja loobunud
}
