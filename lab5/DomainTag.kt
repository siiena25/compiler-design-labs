package laba5

enum class DomainTag(val regex: Regex) {
    IDENT(Regex("[a-zA-Z]+[0-9]*")),
    KEYWORD(Regex("(def|return)")),
    NUMBER(Regex("[0-9]")),
    OPERATION(Regex("([(]|[)]|[:])")),
    PRECOMMENT1(Regex("\"")),
    PRECOMMENT2(Regex("\".{2}")),
    PRECOMMENT3(Regex("\".{3}")),
    PRECOMMENT4(Regex("\"")),
    PRECOMMENT5(Regex("\".{2}")),
    COMMENT(Regex("\".{3}[.]+\".{3}")),
    WHITESPACE(Regex("\\s+")),
    ERROR(Regex("")),
    EOF(Regex(""))
}