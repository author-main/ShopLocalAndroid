package com.training.shoplocal.classes.searcher

import com.training.shoplocal.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.training.shoplocal.log
import com.training.shoplocal.viewmodel.RepositoryViewModel

@Composable
fun ShowSearchHistory(textSearch: State<String>){//}, history: List<String>, actionClear: () -> Unit){
    val viewModel: RepositoryViewModel = viewModel()
    val history = remember {
        mutableStateListOf<String>()
    }

    DisposableEffect(Unit) {
        history.addAll(viewModel.getSearchHistoryList())
        onDispose(){
            log("dispose history")
            viewModel.disposeSearchHistoryList()
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.primary)
    ){
        Column(Modifier.padding(all = 8.dp)){
            log("recomposition history ${textSearch.value}")
            if (textSearch.value.isBlank())
            Row(modifier = Modifier.fillMaxWidth()){
                Text(modifier = Modifier.weight(1f), text = stringResource(id = R.string.text_history))
                Text(modifier = Modifier
                    .clickable {
                 //   actionClear.invoke()
                },
                    text = stringResource(id = R.string.text_clear))
            }
            //LazyColumn(content = )
        }

    }
}