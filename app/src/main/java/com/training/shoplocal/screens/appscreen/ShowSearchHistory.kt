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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.training.shoplocal.log
import com.training.shoplocal.ui.theme.TextFieldBg
import com.training.shoplocal.ui.theme.TextFieldFont
import com.training.shoplocal.viewmodel.RepositoryViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShowSearchHistory(textSearch: MutableState<String>, lastSearch: State<String>){//, callback: (value: String) -> Unit){//}, history: List<String>, actionClear: () -> Unit){
    val viewModel: RepositoryViewModel = viewModel()

    var filtered by remember {
        mutableStateOf(true)
    }

    val showList = remember {
        mutableStateListOf<String>()
    }

   /* LaunchedEffect(lastSearch.value) {
        log("put ${lastSearch.value}")
        if (lastSearch.value.isNotBlank()) {
            viewModel.putSearchHistoryQuery(lastSearch.value)
        }
    }*/

    LaunchedEffect(textSearch.value) {
        val query = textSearch.value
        if (query.isNotBlank()) {
            filtered = true
            showList.apply {
                clear()
                addAll(viewModel.getSearchHistoryList().filter { text ->
                    text.startsWith(query, true)
                })
                //        log(filteredList.toString())
            }
        } else {
            filtered = false
            if (!showList.containsAll(viewModel.getSearchHistoryList()))
            showList.apply {
                clear()
                addAll(viewModel.getSearchHistoryList())
                //log("restore history")
            }
        }
    }

    //log("search ${textSearch.value}")
    DisposableEffect(Unit) {
        showList.addAll(viewModel.getSearchHistoryList())
        onDispose(){
            if (lastSearch.value.isNotBlank()) {
                viewModel.putSearchHistoryQuery(lastSearch.value)
                log("put search query ${lastSearch.value}")
            }
            viewModel.saveSearchHistory()
            viewModel.disposeSearchHistoryList()
            showList.clear()
        }
    }
    //val textFont = FontFamily(Font(R.font.robotocondensed_light))
    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.primary)
    ){
        //log("recompose")
        Column(Modifier.padding(vertical = 8.dp, horizontal = 16.dp)){
         //   log("recomposition history ${textSearch.value}")
            if (textSearch.value.isBlank())
            Row(modifier = Modifier.fillMaxWidth()){
                Text(modifier = Modifier.weight(1f), text = stringResource(id = R.string.text_history))
                Text(modifier = Modifier
                    .clickable {
                    viewModel.clearSearchHistory()
                    showList.clear()
                    //history.clear()
                },
                    text = stringResource(id = R.string.text_clear))
            }
            LazyColumn(modifier = Modifier
                .fillMaxSize()
//                .background(Color.DarkGray)
                .padding(vertical = 8.dp)
            ){
                itemsIndexed(showList){index, line ->
                    Column() {
                        Row(
                            modifier = Modifier//.height(32.dp)
                                //   .padding(vertical = 8.dp)
                                .animateItemPlacement()
                                .clickable {
                                       textSearch.value = line
                                    //callback(line)
                                },
                            //.padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 8.dp)
                                    .padding(end = 8.dp),
                                text = line,
                                color = TextFieldFont,
                                fontSize = 14.sp/*,
                                fontFamily = textFont*/
                            )
                            if (!filtered)
                            Icon(
                                modifier = Modifier
                                    //.align(Alignment.CenterVertically)
                                    .background(TextFieldBg, CircleShape)
                                    .size(16.dp)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) {
                                        //history.removeAt(index)
                                        viewModel.removeSearchHistoryQuery(index)
                                        showList.removeAt(index)
                                    },
                                imageVector = Icons.Filled.Close,
                                contentDescription = null,
                                tint = TextFieldFont.copy(alpha = 0.7f)
                            )
                        }
                        if (index != showList.size-1)
                        Spacer(
                            Modifier
                                .height(1.dp)
                                .fillMaxWidth()
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            MaterialTheme.colors.primary,
                                            Color(0x1BDDDDDD),
                                            MaterialTheme.colors.primary
                                        )
                                    )
                                )
                        )
                    }
                }
            }
        }

    }
}