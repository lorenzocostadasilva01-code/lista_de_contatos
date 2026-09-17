package com.example.lista_de_contato

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lista_de_contato.ui.theme.Lista_de_contatoTheme


class TelaInicial : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lista_de_contatoTheme(){
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Usando Column para organizar os botões um abaixo do outro
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        MudarTela()
                        BotaoToast()
                    }
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun BotaoToast() {
    val context = LocalContext.current

    Button(onClick = {
        // Adicionado o .show() no final!
        Toast.makeText(context, "Olá, sou o toast >_<", Toast.LENGTH_LONG).show()
    }) {
        Text("Mostrar Toast")
    }
}

@Preview(showBackground = true)
@Composable
fun MudarTela() {
    val context = LocalContext.current

    Button(onClick = {
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    }) {
        Text("Ir para outra tela")
    }
}