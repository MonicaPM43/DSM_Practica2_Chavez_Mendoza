open class Cuenta(private val saldo:Float, private var tasaAnual:Float ){
    protected var numConsignaciones:Int = 0
    protected var numRetiros: Int = 0
    protected var tasaAnual: Float = 0.0
    protected var comisionMensual: Float= 0.0

    open fun consignar(cantidad: Float ){
        saldo +=cantidad
        numConsignaciones++
    }

    open fun retirar(cantidad: Float){
        if(dinero>saldo){
            println("Saldo insuficiente, ingrese una cantidad menor")
        }else{
            saldo -= cantidad
            numRetiros++
        }
    }
    open fun actualizar(){
        var interes: Float=0.0
        interes = tasaAnual*saldo/12
        saldo += interes
    }
    open fun extracto(){
        actualizar()
        saldo -= comisionMensual
    }
    open fun imprimir(){
        println("-----------------")
        println("Comision mensual: $comisionMensual")
        println("Numeros de consignaciones: $numConsignaciones")
        println("Numero de retiros: $numRetiros")
        println("Tasa Anual: %$(tasaAnual*100)")
        println("Saldo: $saldo")
    }
}
class CuentaAhorros(private var saldo: Float, private var tasaAnual: Float ):Cuenta(saldo, tasaAnual){
    protected var status: Bollean=true
    open fun asignarStatus(saldo: Float){
 if(saldo<10000){
            status = false
        }else{status=true}
   }
   override fun consignar(cantidad: Float){
    if(status){
        super.consignar(cantidad)
    }else{
        println("Su cuenta no esta activa, no se puede retirar.Dirigase a ventanilla.")
    }
   }
   override fun retirar(cantidad: Float){
    if(status){
        super.retirar(cantidad)
    }else{
        println("Su cuenta no esta activa, no se puede retirar.Dirigase a ventanilla.")
    }
   }
   override fun extracto(){
    super.extracto()
    if(numRetiros>4){
        comisionMensual += (numRetiros-4)*1000
        if(saldo>10000){
            status=true
        }else{status=false}
    }
   }
   override fun imprimir(){
    super.imprimir()
    println("Total de transacciones: $numRetiros+$numConsignaciones")
   }
}
class CuentaCorriente(private var saldo: Float, private var tasAnual: Float):Cuenta(saldo,tasaAnual){
    protected var sobregiro: Float = 0.0
    override fun retirar(cantidad:Float){
        if(cantidad>saldo){
            sobregiro=cantidad-saldo
        }
    } 
    override fun consignar(cantidad: Float){
        super.consignar
    }  }
