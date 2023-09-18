package com.deeosoft.pasteltest.infrastructure.util

import android.content.Context
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ConnectionTest{

    @Mock
    private lateinit var context: Context
    private lateinit var connection: Connection

    @BeforeEach
    fun setUp(){
        MockitoAnnotations.openMocks(this)
        connection = Connection(context)
    }

    @DisplayName("determine upstream link bps is not null")
    fun determineUpStream(){

    }

}