package com.training.shoplocal.screens.mainscreen.composable

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import com.training.shoplocal.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.training.shoplocal.classes.searcher.SearchState
import com.training.shoplocal.log
import com.training.shoplocal.ui.theme.PrimaryDark
import com.training.shoplocal.ui.theme.TextFieldBg
import com.training.shoplocal.ui.theme.TextFieldFont
import com.training.shoplocal.viewmodel.RepositoryViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
//fun ShowSearchHistory(textSearch: MutableState<String>, onSearch: State<Boolean>lastSearch: State<String>){//, callback: (value: String) -> Unit){//}, history: List<String>, actionClear: () -> Unit){
fun ShowSearchHistory(textSearch: MutableState<String>, searchState: State<SearchState>){//, callback: (value: String) -> Unit){//}, history: List<String>, actionClear: () -> Unit){
   // log ("textSearch " + textSearch.value)
    /*val prevTextSearch = remember {
        textSearch.value
    }*/

    val viewModel: RepositoryViewModel = viewModel()

  /*  var filtered by remember {
        mutableStateOf(false)
    }*/

    var initHistoryList by remember {
        mutableStateOf(false)
    }

    val showList = remember {
        mutableStateListOf<String>()
    }

    if (initHistoryList && searchState.value != SearchState.SEARCH_NONE) {
        if (textSearch.value.isNotBlank()) {
            val query = textSearch.value.trim()
            showList.apply {
                clear()
                addAll(viewModel.getSearchHistoryList().filter { text ->
                    text.contains(query, true)
                })
            }
        } else {
            try {
                if (!showList.containsAll(viewModel.getSearchHistoryList()))
                    showList.apply {
                        clear()
                        addAll(viewModel.getSearchHistoryList())
                    }
            } catch (_: Exception) {
                showList.clear()
            }
            //filtered = false
        }
    }

    DisposableEffect(Unit) {
        showList.addAll(viewModel.getSearchHistoryList(true))
        initHistoryList = true
        onDispose {
            //log("on dispose....")
            if (searchState.value == SearchState.SEARCH_RESULT)
                viewModel.putSearchHistoryQuery(textSearch.value.trim())
            viewModel.saveSearchHistory()
            viewModel.disposeSearchHistoryList()
            showList.clear()
        }
    }
    //val textFont = FontFamily(Font(R.font.robotocondensed_light))
    //val interaction = remember { MutableInteractionSource() }
    Box(modifier = Modifier
        .clickable(
            interactionSource = MutableInteractionSource(),
            indication = null,
            onClick = {}
        )
        .fillMaxSize()
        .background(PrimaryDark)
    ){
        //log("recompose")
        if (searchState.value == SearchState.SEARCH_QUERY)
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
                    Column(
                        modifier = Modifier
                                  .animateItemPlacement(
                                      /*  animationSpec = tween(
                                            delayMillis = 150,
                                            easing = FastOutLinearInEasing
                                        )*/
                                    )
                    ) {
                        Row(
                            modifier = Modifier//.height(32.dp)
                                //   .padding(vertical = 8.dp)
                      /*          .animateItemPlacement(
                                    animationSpec = tween(
                                        delayMillis = 1200,
                                        easing = FastOutLinearInEasing
                                    )
                                )*/
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
                            //if (!filtered)
                            //if (!isFiltered.value)
                          //  if (searchState.value == SearchState.SEARCH_QUERY)
                            if (textSearch.value.isBlank())
                            Icon(
                                modifier = Modifier
                                    //.align(Alignment.CenterVertically)
                                    .background(TextFieldBg, CircleShape)
                                    .size(16.dp)
                                    .clickable(
                                        interactionSource = MutableInteractionSource() ,
                                        indication = null
                                    ) {
                                        //history.removeAt(index)
                                        //log("query = $line")
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


// * Добавить индекс XAMMP -> MySQL -> Admin:
// ALTER TABLE `products` ADD FULLTEXT INDEX `product_search_idx` (`name` ASC, `description` ASC);
//SELECT * FROM `products` WHERE MATCH (name) AGAINST (textSearch);