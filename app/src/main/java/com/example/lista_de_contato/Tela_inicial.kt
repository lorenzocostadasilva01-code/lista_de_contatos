package com.example.lista_de_contato

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

data class Contato(
    val nome: String,
    val email: String,
    val telefone: String
)

@Preview(showBackground = true)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val contatos = remember {
        mutableStateListOf(
            Contato("Ana Silva", "ana@email.com", "(41) 99999-1111"),
            Contato("Carlos Oliveira", "carlos@email.com", "(41) 99999-2222")
        )
    }

    NavHost(
        navController = navController,
        startDestination = "lista"
    ) {
        // Rota 1: Lista de Contatos
        composable("lista") {
            TelaListaContatos(
                contatos = contatos,
                onItemClick = { index ->
                    navController.navigate("detalhes/$index")
                },
                onEditClick = { index ->
                    navController.navigate("editar/$index")
                },
                onDeleteClick = { index ->
                    contatos.removeAt(index)
                },
                onAdicionarClick = {
                    navController.navigate("adicionar")
                }
            )
        }

        // Rota 2: Adicionar Contato
        composable("adicionar") {
            TelaAdicionarContato(
                onSalvarClick = { novoContato ->
                    contatos.add(novoContato)
                    navController.popBackStack()
                },
                onCancelarClick = {
                    navController.popBackStack()
                }
            )
        }

        // Rota 3: Detalhes do Contato
        composable(
            route = "detalhes/{index}",
            arguments = listOf(navArgument("index") { type = NavType.IntType })
        ) { backStackEntry ->
            val index = backStackEntry.arguments?.getInt("index") ?: 0
            val contato = contatos.getOrNull(index) ?: Contato("", "", "")

            TelaDetalhesContato(
                contato = contato,
                onVoltarClick = { navController.popBackStack() }
            )
        }

        // Rota 4: Editar Contato
        composable(
            route = "editar/{index}",
            arguments = listOf(navArgument("index") { type = NavType.IntType })
        ) { backStackEntry ->
            val index = backStackEntry.arguments?.getInt("index") ?: 0
            val contatoAtual = contatos.getOrNull(index) ?: Contato("", "", "")

            TelaEditarContato(
                contatoAtual = contatoAtual,
                onSaveClick = { contatoEditado ->
                    contatos[index] = contatoEditado
                    navController.popBackStack()
                },
                onCancelClick = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun TelaListaContatos(
    contatos: List<Contato>,
    onItemClick: (Int) -> Unit,
    onEditClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    onAdicionarClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = onAdicionarClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adicionar Contato"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "--- LISTA DE CONTATOS ---",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(contatos) { index, contato ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onItemClick(index) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = contato.nome,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.weight(1f)
                            )

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                OutlinedButton(
                                    onClick = { onEditClick(index) },
                                    modifier = Modifier.padding(end = 4.dp)
                                ) {
                                    Text("Editar")
                                }

                                IconButton(
                                    onClick = { onDeleteClick(index) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Excluir Contato",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TelaDetalhesContato(
    contato: Contato,
    onVoltarClick: () -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = contato.nome,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = contato.telefone,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = contato.email,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.outline
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onVoltarClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Voltar para Tela Inicial")
            }
        }
    }
}

@Composable
fun TelaEditarContato(
    contatoAtual: Contato,
    onSaveClick: (Contato) -> Unit,
    onCancelClick: () -> Unit
) {
    var nome by remember { mutableStateOf(contatoAtual.nome) }
    var email by remember { mutableStateOf(contatoAtual.email) }
    var telefone by remember { mutableStateOf(contatoAtual.telefone) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Editar Contato",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-mail") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = telefone,
                onValueChange = { telefone = it },
                label = { Text("Telefone") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(onClick = onCancelClick) {
                    Text("Cancelar")
                }

                Button(
                    onClick = {
                        if (nome.isNotBlank()) {
                            onSaveClick(Contato(nome, email, telefone))
                        }
                    }
                ) {
                    Text("Salvar")
                }
            }
        }
    }
}