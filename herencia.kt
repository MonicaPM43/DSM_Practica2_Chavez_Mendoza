// Clase base Cuenta
open class Cuenta(protected var saldo: Float, protected var tasaAnual: Float) {
    // Atributos protegidos (accesibles desde las clases hijas)
    protected var numConsignaciones: Int = 0
    protected var numRetiros: Int = 0
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
class CuentaAhorros(saldo: Float, tasaAnual: Float) : Cuenta(saldo, tasaAnual) {
    private var activa: Boolean = saldo >= 10000   // Una cuenta está activa si saldo >= 10000

    // Método privado para verificar si la cuenta sigue activa
    private fun actualizarEstado() {
        activa = saldo >= 10000
    }

    // Redefinición de consignar
    override fun consignar(cantidad: Float) {
        if (activa) {
            super.consignar(cantidad)
            actualizarEstado()
        } else {
            println("La cuenta está inactiva. No se puede consignar.")
        }
    }

    // Redefinición de retirar
    override fun retirar(cantidad: Float) {
        if (activa) {
            super.retirar(cantidad)
            actualizarEstado()
        } else {
            println("La cuenta está inactiva. No se puede retirar.")
        }
    }

    // Redefinición de extracto mensual
    override fun extracto() {
        if (numRetiros > 4) {
            // Por cada retiro adicional a 4 se cobra 1000
            comisionMensual += (numRetiros - 4) * 1000
        }
        super.extracto()
        actualizarEstado()
    }

    // Nuevo imprimir para mostrar transacciones
    override fun imprimir() {
        super.imprimir()
        println("Cuenta activa: $activa")
        println("Total de transacciones: ${numConsignaciones + numRetiros}")
    }
}

// Clase hija CuentaCorriente
class CuentaCorriente(saldo: Float, tasaAnual: Float) : Cuenta(saldo, tasaAnual) {
    private var sobregiro: Float = 0.0f   // Valor de sobregiro (cuando retira más del saldo)

    // Redefinición de retirar: permite sobregiro
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

    // Redefinición de consignar: primero cubre sobregiro
    override fun consignar(cantidad: Float) {
        if (sobregiro > 0) {
            if (cantidad >= sobregiro) {
                // Si lo consignado cubre el sobregiro
                val resto = cantidad - sobregiro
                sobregiro = 0.0f
                saldo += resto
            } else {
                // Solo se reduce el sobregiro
                sobregiro -= cantidad
            }
        } else {
            saldo += cantidad
        }
        numConsignaciones++
    }

    // Redefinición de imprimir
    override fun imprimir() {
        super.imprimir()
        println("Sobregiro: $sobregiro")
        println("Total de transacciones: ${numConsignaciones + numRetiros}")
    }
}

// Método main para probar
fun main() {
    // Crear una cuenta de ahorros
    val cuentaAhorros = CuentaAhorros(12000f, 0.05f)  // saldo inicial = 12000, tasa anual 5%

    cuentaAhorros.imprimir()

    // Hacemos algunas operaciones
    cuentaAhorros.retirar(3000f)
    cuentaAhorros.consignar(2000f)
    cuentaAhorros.retirar(5000f)
    cuentaAhorros.retirar(1000f)
    cuentaAhorros.retirar(500f)  // aquí ya serían más de 4 retiros, se cobrará comisión

    // Generar extracto mensual
    cuentaAhorros.extracto()
    cuentaAhorros.imprimir()

    println("\n---- Cuenta Corriente ----")
    val cuentaCorriente = CuentaCorriente(5000f, 0.04f)
    cuentaCorriente.imprimir()

    cuentaCorriente.retirar(8000f)  // genera sobregiro
    cuentaCorriente.imprimir()

    cuentaCorriente.consignar(4000f)  // reduce sobregiro
    cuentaCorriente.imprimir()
}
