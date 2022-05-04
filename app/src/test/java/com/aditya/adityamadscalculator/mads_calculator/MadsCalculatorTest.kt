package com.aditya.adityamadscalculator.mads_calculator

import com.aditya.adityamadscalculator.ui.CalculatorActivity
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MadsCalculatorTest {

    @Test
    fun givenValidInputAdd_applyOperation() {
        val b = 20
        val c = 30
        CalculatorActivity.applyOperation('+', b, c)
    }

    @Test
    fun givenValidInputSub_applyOperation() {
        val b = 20
        val c = 30
        CalculatorActivity.applyOperation('-', b, c)
    }

    @Test
    fun givenValidInputMul_applyOperation() {
        val b = 20
        val c = 30
        CalculatorActivity.applyOperation('*', b, c)
    }

    @Test
    fun givenValidInputDiv_applyOperation() {
        val b = 20
        val c = 30
        CalculatorActivity.applyOperation('/', b, c)
    }

}