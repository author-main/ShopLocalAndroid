package com.training.shoplocal.screens.appscreen

import androidx.compose.foundation.ExperimentalFoundationApi
import com.training.shoplocal.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.training.shoplocal.log
import com.training.shoplocal.ui.theme.TextFieldBg
import com.training.shoplocal.ui.theme.TextFieldFont
import com.training.shoplocal.viewmodel.RepositoryViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShowSearchHistory(textSearch: State<String>, callback: (value: String) -> Unit){//}, history: List<String>, actionClear: () -> Unit){
    val viewModel: RepositoryViewModel = viewModel()
    val history = remember {
        mutableStateListOf<String>()
    }

    DisposableEffect(Unit) {
        history.addAll(viewModel.getSearchHistoryList())
        onDispose(){
           // log("dispose history")
           // textSearch.value = ";jgf"
            viewModel.disposeSearchHistoryList()
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.primary)
    ){
        Column(Modifier.padding(vertical = 8.dp, horizontal = 16.dp)){
         //   log("recomposition history ${textSearch.value}")
            if (textSearch.value.isBlank())
            Row(modifier = Modifier.fillMaxWidth()){
                Text(modifier = Modifier.weight(1f), text = stringResource(id = R.string.text_history))
                Text(modifier = Modifier
                    .clickable {

                    history.clear()
                },
                    text = stringResource(id = R.string.text_clear))
            }
            LazyColumn(modifier = Modifier.fillMaxSize()
//                .background(Color.DarkGray)
                .padding(vertical = 8.dp)
            ){
                itemsIndexed(history){index, line ->
                        Row(
                            modifier = Modifier.height(32.dp)
                                .animateItemPlacement()
                                .clickable {
                                    callback(line)
                                },
                            //.padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = line,
                                color = TextFieldFont
                            )
                            Icon(
                                modifier = Modifier
                                    //.align(Alignment.CenterVertically)
                                    .size(16.dp)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) {
                                        history.removeAt(index)
                                    },
                                imageVector = Icons.Filled.Close,
                                contentDescription = null,
                                tint = TextFieldFont
                            )
                        }
                }
            }
        }

    }
}