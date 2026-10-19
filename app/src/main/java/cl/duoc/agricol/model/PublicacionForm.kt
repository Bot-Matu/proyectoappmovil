package cl.duoc.agricol.model

import java.util.Date

data class PublicacionForm(val titulo:String="",
                           val comuna:String="",
                           val ciudad:String="",
                           val fecha_inicio:Date?=null,
                           val fecha_termino:Date?=null,
                           val tra_necesitados:Int?=null,
                           val descripcion:String="",
                           val info_extra:String="",
                           val paga:Double=0.0,
                           )
