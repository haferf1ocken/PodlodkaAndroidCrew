package com.example.podlodkaandroidcrew.ui.home

import android.app.Activity
import android.content.res.Configuration
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import com.example.podlodkaandroidcrew.R
import com.example.podlodkaandroidcrew.data.model.Session
import com.example.podlodkaandroidcrew.ui.UiState
import com.example.podlodkaandroidcrew.ui.theme.PodlodkaAndroidCrewTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SessionSearchTextField(
    textValue: MutableState<String>,
    modifier: Modifier
) {
    val localFocusManager = LocalFocusManager.current

    OutlinedTextField(
        value = textValue.value,
        onValueChange = {
            textValue.value = it
        },
        label = {
            Text(
                text = stringResource(id = R.string.search),
                fontWeight = FontWeight.Light
            )
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = null)
        },
        modifier = modifier
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                localFocusManager.clearFocus()
            }
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            cursorColor = MaterialTheme.colors.onBackground,
            focusedBorderColor = MaterialTheme.colors.onBackground,
            leadingIconColor = MaterialTheme.colors.onBackground,
            focusedLabelColor = MaterialTheme.colors.onBackground
        )
    )
}

@ExperimentalAnimationApi
@Composable
fun FavoritesSection(
    favouritesSessions: List<Session>,
    isVisible: Boolean,
    navigateToSession: (String) -> Unit,
    modifier: Modifier
) {
    AnimatedVisibility(visible = isVisible) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Text(
                text = stringResource(R.string.favourites),
                style = MaterialTheme.typography.h6,
                modifier = modifier
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                items(favouritesSessions) { session ->
                    FavoritesSessionCard(
                        session = session,
                        navigateToSession = navigateToSession
                    )
                }
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
fun SessionsSection(
    sessionsWithDates: Map<String, List<Session>>,
    showSnackbar: () -> Unit,
    favorites: Set<Session>,
    onToggleFavorite: (Session) -> Unit,
    navigateToSession: (String) -> Unit,
    filterValue: String,
    modifier: Modifier = Modifier
) {
    Column {
        sessionsWithDates.toList().forEach { (date, sessions) ->
            val filteredSessions = sessions.filter {
                it.speaker
                    .toLowerCase(Locale.current)
                    .contains(filterValue.toLowerCase(Locale.current))
            }
            if (filteredSessions.isNotEmpty()) {
                SessionDate(date = date)
                filteredSessions.forEach { session ->
                    SessionCardSimple(
                        session = session,
                        navigateToSession = navigateToSession,
                        isFavorite = favorites.contains(session),
                        onToggleFavorite = {
                            if (favorites.size < 3 || favorites.contains(session)) {
                                onToggleFavorite(session)
                            } else {
                                showSnackbar()
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SessionDate(
    date: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = date,
        fontSize = 18.sp,
        fontWeight = FontWeight.Light,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
    )
}

@ExperimentalAnimationApi
@ExperimentalCoilApi
@ExperimentalFoundationApi
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navigateToSession: (String) -> Unit
) {
    val sessionsWithDates by remember(viewModel) { viewModel.sessionsWithDates }.collectAsState()
    val favorites by remember(viewModel) { viewModel.favorites }.collectAsState(setOf())
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val openDialog = remember { mutableStateOf(false)  }
    BackHandler(enabled = true) {
        openDialog.value = true
    }

    if (openDialog.value) {
        ExitConfirmDialog(openDialog = openDialog)
    }

    Scaffold(
        scaffoldState = scaffoldState
    ) {
        when(sessionsWithDates) {
            is UiState.Loading -> {
                FullScreenLoading()
            }
            is UiState.Success -> {
                HomeContent(
                    favorites,
                    navigateToSession,
                    (sessionsWithDates as UiState.Success<Map<String, List<Session>>>).data,
                    coroutineScope,
                    scaffoldState,
                    viewModel
                )
            }
            is UiState.Error -> {
                Toast.makeText(LocalContext.current, (sessionsWithDates as UiState.Error).errorMessage, Toast.LENGTH_SHORT).show()
            }
            is UiState.Default -> {}
        }
    }
}

@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
private fun HomeContent(
    favorites: Set<Session>,
    navigateToSession: (String) -> Unit,
    sessionsWithDates: Map<String, List<Session>>,
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState,
    viewModel: HomeViewModel
) {
    val textValue = rememberSaveable {
        mutableStateOf("")
    }

    LazyColumn {
        item {
            SessionSearchTextField(
                textValue = textValue,
                modifier = Modifier.padding(16.dp)
            )
        }
        item {
            FavoritesSection(
                favouritesSessions = favorites.toList(),
                isVisible = favorites.isNotEmpty(),
                navigateToSession = navigateToSession,
                modifier = Modifier.padding(16.dp)
            )
        }
        stickyHeader {
            SessionHeader()
        }
        item {
            SessionsSection(
                sessionsWithDates = sessionsWithDates,
                navigateToSession = navigateToSession,
                showSnackbar = {
                    coroutineScope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = "Не удалось добавить сессию в избранное",
                            duration = SnackbarDuration.Short
                        )
                    }
                },
                favorites = favorites,
                onToggleFavorite = { viewModel.toggleFavorite(it) },
                filterValue = textValue.value,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Composable
private fun SessionHeader() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(60.dp)
            .padding(bottom = 8.dp)
            .background(MaterialTheme.colors.background)
    ) {
        Text(
            text = stringResource(R.string.sessions),
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .padding(start = 16.dp)
        )
    }
}

@Composable
private fun FullScreenLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colors.onBackground
        )
    }
}

@Composable
fun ExitConfirmDialog(
    openDialog: MutableState<Boolean>
) {
    val activity = LocalContext.current as Activity

    AlertDialog(
        onDismissRequest = {
            openDialog.value = false
        },
        title = {
            Text(text = stringResource(R.string.exit_dialog_title))
        },
        text = {
            Text(stringResource(R.string.exit_dialog_text))
        },
        confirmButton = {
            val confirmButtonInteractionSource = remember { MutableInteractionSource() }
            val isConfirmButtonPressed by confirmButtonInteractionSource.collectIsPressedAsState()
            val confirmButtonColor = if (isConfirmButtonPressed) MaterialTheme.colors.onBackground.copy(alpha = 0.7f)
                else MaterialTheme.colors.onBackground

            Button(
                onClick = {
                    openDialog.value = false
                    activity.finish()
                },
                interactionSource = confirmButtonInteractionSource,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = confirmButtonColor,
                    contentColor = MaterialTheme.colors.background
                )
            ) {
                Text(stringResource(R.string.confirm_button))
            }
        },
        dismissButton = {
            val dismissButtonInteractionSource = remember { MutableInteractionSource() }
            val isDismissButtonPressed by dismissButtonInteractionSource.collectIsPressedAsState()
            val dismissButtonColor = if (isDismissButtonPressed) MaterialTheme.colors.onBackground.copy(alpha = 0.7f)
                else MaterialTheme.colors.onBackground

            Button(
                onClick = { openDialog.value = false },
                interactionSource = dismissButtonInteractionSource,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = dismissButtonColor,
                    contentColor = MaterialTheme.colors.background
                )
            ) {
                Text(stringResource(R.string.dismiss_button))
            }
        }
    )
}

@Preview("Session search text field")
@Preview("Session search text field (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SessionSearchTextFieldPreviewFavoritesSectionPreview() {
    PodlodkaAndroidCrewTheme {
        Surface {
            val textValue = remember {
                mutableStateOf("")
            }
            SessionSearchTextField(textValue, Modifier.padding(8.dp))
        }
    }
}

@ExperimentalAnimationApi
@Preview("Favorites section")
@Preview("Favorites section (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FavoritesSectionPreview() {
    PodlodkaAndroidCrewTheme {
        Surface {
            val isVisible by remember {
                mutableStateOf(true)
            }
            FavoritesSection(
                favouritesSessions = listOf(),
                navigateToSession = {},
                isVisible = isVisible,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@ExperimentalCoilApi
@Preview("Session section")
@Preview("Session section (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SessionSectionPreview() {
    PodlodkaAndroidCrewTheme {
        Surface {
            SessionsSection(mapOf(), {}, setOf(), {}, {}, "", Modifier.padding(8.dp))
        }
    }
}

//@ExperimentalAnimationApi
//@ExperimentalCoilApi
//@ExperimentalFoundationApi
//@Preview("Home screen")
//@Preview("Home screen (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun HomeScreenPreview() {
//    PodlodkaAndroidCrewTheme {
//        Surface {
//            HomeScreen(HomeViewModel(PodlodkaAndroidCrewApplication().repository, navigateToSession = {})
//        }
//    }
//}
