package com.plcoding.mvvmtodoapp.ui.todo_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.plcoding.mvvmtodoapp.util.UiEvent
import kotlinx.coroutines.flow.collect


@Composable
fun TodoListScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: TodoListViewModel = hiltViewModel()
) {
    val todos = viewModel.todos.collectAsState(initial = emptyList())
    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when(event) {
                is UiEvent.ShowSnackbar -> {
                    val result = scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action
                    )
                    if(result == SnackbarResult.ActionPerformed) {
                        viewModel.onEvent(TodoListEvent.OnUndoDeleteClick)
                    }
                }
                is UiEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }
    Scaffold(
        floatingActionButtonPosition = FabPosition.End,
        scaffoldState = scaffoldState,

        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(TodoListEvent.OnAddTodoClick)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        }
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Habit App",
                    color = Color.White
                )
            },
//            backgroundColor = colorResource(id = R.color.purple_700),
            contentColor = Color.White,
            elevation = 12.dp,
            actions = {
                IconButton(onClick = { viewModel.onEvent(TodoListEvent.OnAddTodoClick) }) {
                    Icon(Icons.Filled.Add, contentDescription = "Localized description")
                }
                IconButton(onClick = { viewModel.onEvent(TodoListEvent.OnAddTodoClick) }) {
                    Icon(Icons.Filled.Settings, contentDescription = "Localized description")
                }
            }
        )


            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(top = 58.dp)
            ) {

                items(todos.value) { todo ->
                    Card(
                        modifier = Modifier.padding(15.dp),
                        elevation = 4.dp,
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        TodoItem(
                            todo = todo,
                            onEvent = viewModel::onEvent,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.onEvent(TodoListEvent.OnTodoClick(todo))
                                }
                                .padding(16.dp)
                        )
                    }
                }
        }
    }
}