// Clase base Cuenta
open class Cuenta(saldoInicial: Float, tasaAnualInicial: Float) {
    // Atributos protegidos
    protected var saldo: Float = saldoInicial
    protected var numConsignaciones: Int = 0
    protected var numRetiros: Int = 0
    protected var tasaAnual: Float = tasaAnualInicial
    protected var comisionMensual: Float = 0.0f

    // Método para consignar dinero
    open fun consignar(cantidad: Float) {
        saldo += cantidad
        numConsignaciones++
    }

    // Método para retirar dinero (si hay saldo suficiente)
    open fun retirar(cantidad: Float) {
        if (cantidad > saldo) {
            println("Saldo insuficiente, ingrese una cantidad menor")
        } else {
            saldo -= cantidad
            numRetiros++
        }
    }

    // Método para calcular y actualizar el saldo con el interés mensual
    open fun actualizar() {
        val interes: Float = (tasaAnual / 12) * saldo
        saldo += interes
    }

    // Método que descuenta comisión mensual y actualiza el interés
    open fun extracto() {
        saldo -= comisionMensual
        actualizar()
    }

    // Método para imprimir los valores de los atributos
    open fun imprimir() {
        println("----------- CUENTA -----------")
        println("Saldo: $saldo")
        println("Comisión mensual: $comisionMensual")
        println("Número de consignaciones: $numConsignaciones")
        println("Número de retiros: $numRetiros")
        println("Tasa Anual: ${tasaAnual * 100}%")
    }
}

// Clase hija CuentaAhorros
class CuentaAhorros(saldoInicial: Float, tasaAnualInicial: Float) : Cuenta(saldoInicial, tasaAnualInicial) {
    private var activa: Boolean = saldo >= 10000   // Activa si saldo >= 10000

    private fun actualizarEstado() {
        activa = saldo >= 10000
    }

    override fun consignar(cantidad: Float) {
        if (activa) {
            super.consignar(cantidad)
            actualizarEstado()
        } else {
            println("La cuenta está inactiva. No se puede consignar.")
        }
    }

    override fun retirar(cantidad: Float) {
        if (activa) {
            super.retirar(cantidad)
            actualizarEstado()
        } else {
            println("La cuenta está inactiva. No se puede retirar.")
        }
    }

    override fun extracto() {
        if (numRetiros > 4) {
            comisionMensual += (numRetiros - 4) * 1000
        }
        super.extracto()
        actualizarEstado()
    }

    override fun imprimir() {
        super.imprimir()
        println("Cuenta activa: $activa")
        println("Total de transacciones: ${numConsignaciones + numRetiros}")
    }
}

// Clase hija CuentaCorriente
class CuentaCorriente(saldoInicial: Float, tasaAnualInicial: Float) : Cuenta(saldoInicial, tasaAnualInicial) {
    private var sobregiro: Float = 0.0f

    override fun retirar(cantidad: Float) {
        if (cantidad > saldo) {
            sobregiro += cantidad - saldo
            saldo = 0.0f
            numRetiros++
        } else {
            saldo -= cantidad
            numRetiros++
        }
    }

    override fun consignar(cantidad: Float) {
        if (sobregiro > 0) {
            if (cantidad >= sobregiro) {
                val resto = cantidad - sobregiro
                sobregiro = 0.0f
                saldo += resto
            } else {
                sobregiro -= cantidad
            }
        } else {
            saldo += cantidad
        }
        numConsignaciones++
    }

    override fun imprimir() {
        super.imprimir()
        println("Sobregiro: $sobregiro")
        println("Total de transacciones: ${numConsignaciones + numRetiros}")
    }
}

// Método main para probar
fun main() {
    println("---- Cuenta de Ahorros ----")
    val cuentaAhorros = CuentaAhorros(12000f, 0.05f) // saldo inicial 12000, tasa 5%
    cuentaAhorros.imprimir()

    cuentaAhorros.retirar(3000f)
    cuentaAhorros.consignar(2000f)
    cuentaAhorros.retirar(5000f)
    cuentaAhorros.retirar(1000f)
    cuentaAhorros.retirar(500f)  // Aquí ya más de 4 retiros

    cuentaAhorros.extracto()
    cuentaAhorros.imprimir()

    println("\n---- Cuenta Corriente ----")
    val cuentaCorriente = CuentaCorriente(5000f, 0.04f)
    cuentaCorriente.imprimir()

    cuentaCorriente.retirar(8000f) // genera sobregiro
    cuentaCorriente.imprimir()

    cuentaCorriente.consignar(4000f) // reduce sobregiro
    cuentaCorriente.imprimir()
}
