package com.training.shoplocal.screens.appscreen

import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.node.modifierElementOf
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.core.graphics.toColor
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.training.shoplocal.R
import com.training.shoplocal.classes.*
import com.training.shoplocal.classes.fodisplay.*
import com.training.shoplocal.getFormattedPrice
import com.training.shoplocal.getStringResource
import com.training.shoplocal.log
import com.training.shoplocal.ui.theme.*
import com.training.shoplocal.viewmodel.RepositoryViewModel
import java.util.*

/*data class ItemFilter(
    val id: Int,
    val name: String,
    var selected: Boolean = false
) {

    fun isHeader() =
        id < 0
    fun isCategory() =
        id == CATEGORY_ITEM
    fun isBrand() =
        id == BRAND_ITEM
}*/

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ShowFilterDisplay(filter: ProviderDataDisplay, reset: () -> Unit, perform: (filter: FilterData)->Unit){
    val viewModel: RepositoryViewModel = viewModel()
    val items = remember {
        viewModel.getSectionFilter()
    }
    val editFilter = remember {
        FilterData(
            viewmode    = filter.getViewMode(),
            brend       = filter.getBrend(),
            favorite    = filter.getFavorite(),
            priceRange  = filter.getPriceRange(),
            category    = filter.getCategory(),
            discount    = filter.getDiscount()
        )
    }
    fun checkSelectedItems(sectionId: Int, ids: String) {
        if (ids != "-1") {
            val itemsId = ids.split(',').map { it.toInt() }
            //val entry = items.entries.filter { it -> it.key.id == sectionId }
            val list = items[sectionId]?.filter { it -> it.id in itemsId } ?: listOf()
            list.forEach {item ->
                item.selected = true
            }

            //find { it -> it.id.toString() in itemsId}.
            /*list?.forEach {item ->
                for (id in itemsId) {
                    if (item.id == id.toInt()) {
                        item.selected = true
                        break
                    }
                }
            }*/
        }
    }


    DisposableEffect(Unit) {
        checkSelectedItems(BREND_ITEM, filter.getBrend())
        checkSelectedItems(CATEGORY_ITEM, filter.getCategory())
       // log(items)
        onDispose {
            items.clear()
        }
    }


    var openSection by remember {
        mutableStateOf(NO_OPEN_ITEM)
    }


    val focusManager = LocalFocusManager.current
    fun checkNumberValue(value: String, len: Int): String? {
        //log("len = $len")
        if (!value.contains(".") && !value.contains(",") && !value.contains("-") && value.length <= len) {
            val number = try {
                value.toInt()
            } catch (_: java.lang.Exception) {
                0
            }
            return number.toString()
        }
        return null
    }
    @Composable
    fun showButtonPanel(){
        val font = remember { FontFamily(Font(R.font.roboto_light)) }
        Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center){
            Button(onClick = {
                reset()
            },
                //shape = RoundedCornerShape(32.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = TextFieldBg)
            ){
                Text(text= stringResource(id = R.string.text_reset),
                    color = TextLightGray,
                    fontFamily = font,
                    letterSpacing = 0.sp
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                val listBrand = items[BREND_ITEM]?.filter {it.selected}?. map { it.id }?.joinToString(",") ?: ANY_VALUE.toString()
                editFilter.brend = listBrand.ifEmpty { ANY_VALUE.toString() }
                val listCategory = items[CATEGORY_ITEM]?.filter {it.selected}?. map { it.id }?.joinToString(",") ?: ANY_VALUE.toString()
                editFilter.category = listCategory.ifEmpty { ANY_VALUE.toString() }
                perform(editFilter)
            },
               // shape = RoundedCornerShape(32.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = TextBrand)
            ){
                Text(text= stringResource(id = R.string.text_perform), color = TextLightGray,
                    fontFamily = font,
                    letterSpacing = 0.sp)
            }
        }
    }

    @Composable
    fun NumberTextField(modifier: Modifier, value: String, label: String, len: Int, onChange: (text: String) ->Unit){
        var number by remember{
            mutableStateOf(value)
        }
        TextField(
            modifier = modifier,
            value = number,//value.value,
            onValueChange = {
                val newValue = checkNumberValue(it, len)
                if (newValue != null) {
                    //value.value = newValue
                    number = newValue
                    onChange(newValue)
                }

            },
            leadingIcon = { Text(text = label,//stringResource(id = R.string.text_to),
                color = TextFieldFont.copy(alpha = 0.5f)
            ) },
            colors = TextFieldDefaults.textFieldColors(
                textColor = TextFieldFont,
                backgroundColor = TextFieldBg,
                cursorColor = TextFieldFont,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Number),
            keyboardActions = KeyboardActions (
                onDone = {
                    focusManager.clearFocus(true)
                }
            )
        )
    }


    @Composable
    fun showRangePrice(){//valueFrom: Float, valueTo: Float){

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = "${stringResource(id = R.string.text_price)}, ${stringResource(id = R.string.text_currency)}",
                color = TextFieldFont
            )
            Row(
               // modifier = Modifier
               //     .padding(all = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NumberTextField(modifier = Modifier.weight(0.5f), value = editFilter.priceRange.first.toInt().toString(),
                    label = stringResource(id = R.string.text_from), len = 6) {
                    val second = editFilter.priceRange.second
                    editFilter.priceRange = it.toFloat() to second
                }

                Spacer(modifier = Modifier.width(8.dp))
                NumberTextField(modifier = Modifier.weight(0.5f), value = editFilter.priceRange.second.toInt().toString(),
                    stringResource(id = R.string.text_to), len = 6) {
                    val first = editFilter.priceRange.first
                    editFilter.priceRange = first to it.toFloat()
                }

            }
        }
    }

    @Composable
    fun showDiscount(value: Int){

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(end = 8.dp),
                text = stringResource(id = R.string.text_discount),
                color = TextFieldFont
            )
            NumberTextField(modifier = Modifier.weight(0.5f), value = editFilter.discount.toString(),
                label = stringResource(id = R.string.text_from),len = 2) {
                editFilter.discount = it.toInt()
            }

            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = "%",
                color = TextFieldFont
            )

            CustomCheckBox(
                modifier = Modifier.padding(start = 24.dp),
                text = stringResource(id = R.string.text_favorite),
                checked = editFilter.favorite == 1
            ) { checked ->
                editFilter.favorite = if (checked) 1 else ANY_VALUE
                focusManager.clearFocus(true)
            }
        }
    }

    @Composable
    fun showViewMode(){
        var mode by remember {
            mutableStateOf(filter.getViewMode())
        }
        @Composable
        fun ModeButton(modifier: Modifier, modeview: VIEW_MODE){
            val unselectedBgColor = remember {
                val hsv = FloatArray(3)
                android.graphics.Color.colorToHSV(TextFieldBg.toArgb(), hsv)
                hsv[2] *= 0.8f
                val currentColor = android.graphics.Color.HSVToColor(hsv)
                Color(currentColor.red, currentColor.green, currentColor.blue)
            }
            val bgcolor = if (modeview == mode)
                TextFieldBg
            else
                unselectedBgColor

            val iconcolor = if (modeview == mode)
                SelectedItemBottomNavi
            else
                TextFieldFont
            val srcImage = if (modeview == VIEW_MODE.CARD)
                R.drawable.ic_cardmode
            else
                R.drawable.ic_rowmode
            OutlinedButton(onClick = {
                focusManager.clearFocus(true)
                mode = modeview
                editFilter.viewmode = modeview
            }, modifier = modifier,
                border = BorderStroke(0.dp, bgcolor),
                colors = ButtonDefaults.buttonColors(backgroundColor = bgcolor
                )
            ) {
                Icon(imageVector = ImageVector.vectorResource(srcImage),
                    contentDescription = null,
                    tint = iconcolor//TextFieldFont
                )
            }
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text = stringResource(id = R.string.text_viewmode), modifier = Modifier.padding(end = 8.dp), color = TextFieldFont)
            Row(modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween) {
                ModeButton(modifier = Modifier.weight(0.5f), VIEW_MODE.CARD)
                Spacer(modifier = Modifier.width(8.dp))
                ModeButton(modifier = Modifier.weight(0.5f), VIEW_MODE.ROW)
            }
        }
    }

    @Composable
    fun showSectionItems(section: Int, list: List<ItemFilter>){
        val expanded = openSection == section
        val visibleItem = remember {
            MutableTransitionState(false)
        }
        visibleItem.targetState = expanded
        androidx.compose.animation.AnimatedVisibility(
            visibleState = visibleItem,
            enter = expandVertically(
                animationSpec = tween(
                    durationMillis = 150,
                    easing = LinearEasing
                )
            ),
            exit =  shrinkVertically  (
                animationSpec = tween(
                    durationMillis = 150,
                    easing = LinearEasing
                )
            )
        ) {
            Column() {
                list.forEach { item ->
                    CustomCheckBox(
                        modifier = Modifier
                            .padding(start = 32.dp)
                            .height(36.dp)
                            .fillMaxWidth(), text = item.name,
                        checked = item.selected
                    ) { checked ->
                        item.selected = checked
                       // log(items)
                    }
                }
            }
        }
    }

    @Composable
    fun showSectionHeader(sectionId: Int){
        /*val textCategory = stringResource(id = R.string.text_category)
        val textBrend    = stringResource(id = R.string.text_brend)*/
        val nameSection = remember {
            when (sectionId) {
                CATEGORY_ITEM   -> getStringResource(R.string.text_category)//stringResource(id = R.string.text_category)
                BREND_ITEM      -> getStringResource(R.string.text_brend)//stringResource(id = R.string.text_brend)
                else            -> null
            }
        }
        if (nameSection != null) {
            val interactionSource = remember { MutableInteractionSource() }
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        openSection = if (openSection != sectionId)
                            sectionId
                        else
                            NO_OPEN_ITEM
                    }
            ) {
                val expanded = openSection == sectionId
                val fontcolor = if (expanded) SelectedItem else TextFieldFont
                if (!expanded)
                Image(
                    Icons.Filled.KeyboardArrowDown,
                    //modifier = Modifier.rotate(90f),
                    modifier = Modifier.size(16.dp),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(fontcolor)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    modifier = Modifier.padding(vertical = 4.dp), text = nameSection,
                    color = fontcolor, fontWeight = if (expanded) FontWeight.Medium else FontWeight.Normal
                )

        }
        } /*else {
          CustomCheckBox(modifier = Modifier
              .padding(start = 32.dp)
              .height(36.dp)
              .fillMaxWidth()
              , text = item.name) { checked ->
                    item.selected = checked
          }
        }*/
    }
    Box(modifier = Modifier
        //.fillMaxSize()
        .background(BgScreenDark)
    ){
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp)) {
            Card(
                backgroundColor = PrimaryDark
            ) {

                showViewMode()

            }
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                /*modifier = Modifier
                    .fillMaxWidth(),*/
                //elevation = 4.dp,
                //shape = RoundedCornerShape(20.dp),

                backgroundColor = PrimaryDark

            ) {

                    showDiscount(value = 20)

            }
            Spacer(modifier = Modifier.height(8.dp))
            Card(
               /* modifier = Modifier
                    .fillMaxWidth(),*/
                backgroundColor = PrimaryDark
            ) {
                showRangePrice()//valueFrom = 500f, valueTo = 2000f)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                ,

                backgroundColor = PrimaryDark
            ) {
                val scrollState = rememberScrollState()
                Box(modifier = Modifier
                    .padding(all = 16.dp)) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(scrollState)
                            .fillMaxWidth()
                    ) {
                        items.forEach { entry ->
                            /*val entrySection = when (entry.key) {
                                CATEGORY_ITEM -> {
                                    ItemFilter(
                                        CATEGORY_ITEM,
                                        stringResource(id = R.string.text_category),
                                    )
                                }
                                BREND_ITEM -> {
                                    ItemFilter(BREND_ITEM, stringResource(id = R.string.text_brend))
                                }
                                else -> null
                            }*/
                            if (entry.key < 0) {
                                showSectionHeader(entry.key)
                                showSectionItems(entry.key, entry.value)
                            }

                            /*entrySection?.let { section ->
                                showSectionHeader(section)
                                showSectionItems(section, entry.value)
                            }*/
                        }

                       /* items.forEach { entry ->
                            val entrySection = when (entry.key) {
                                CATEGORY_ITEM -> {
                                    ItemFilter(
                                        CATEGORY_ITEM,
                                        stringResource(id = R.string.text_category)
                                    )
                                }
                                BREND_ITEM -> {
                                    ItemFilter(BREND_ITEM, stringResource(id = R.string.text_brend))
                                }
                                else -> null
                            }


                            entrySection?.let { section ->
                                showFilterItem(section)
                                //visibleItem.targetState = openSection == section.id
                                    androidx.compose.animation.AnimatedVisibility(
                                        visibleState = visibleItem,
                                        enter = scaleIn(
                                            animationSpec = tween(
                                                durationMillis = 200,
                                                easing = LinearEasing
                                            )
                                        ),
                                        exit =  scaleOut(
                                            animationSpec = tween(
                                                durationMillis = 100,
                                                easing = LinearEasing
                                            )
                                        )
                                    ) {
                                        Column() {
                                            entry.value.forEach { item ->
                                                showFilterItem(item)
                                            }
                                        }
                                    }






                            }
                        }*/

                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            showButtonPanel()
        }
    }
}