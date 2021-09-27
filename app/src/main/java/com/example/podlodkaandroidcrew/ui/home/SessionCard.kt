package com.example.podlodkaandroidcrew.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import com.example.podlodkaandroidcrew.R
import com.example.podlodkaandroidcrew.data.model.Session
import com.example.podlodkaandroidcrew.extensions.truncate
import com.example.podlodkaandroidcrew.ui.session.SessionImage
import com.example.podlodkaandroidcrew.ui.theme.PodlodkaAndroidCrewTheme

@ExperimentalCoilApi
@Composable
fun SessionCardSimple(
    session: Session,
    navigateToSession: (String) -> Unit,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit
) {
    val favoriteAction = stringResource(if (isFavorite) R.string.unfavorite else R.string.favorite)

    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = 10.dp,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .clickable(onClick = { navigateToSession(session.id) })
    ) {
        Row(
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                .semantics {
                    // By defining a custom action, we tell accessibility services that this whole
                    // composable has an action attached to it. The accessibility service can choose
                    // how to best communicate this action to the user.
                    customActions = listOf(
                        CustomAccessibilityAction(
                            label = favoriteAction,
                            action = { onToggleFavorite(); true }
                        )
                    )
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            SessionImage(
                session = session,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(60.dp)
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                SpeakerAndTimeInterval(
                    session = session
                )
                SessionShortDescription(
                    session = session
                )
            }
            FavoriteButton(
                isFavorite = isFavorite,
                onClick = onToggleFavorite,
                modifier = Modifier.clearAndSetSemantics {}
            )
        }
    }
}

@Composable
fun FavoriteButton(
    isFavorite: Boolean,
    onClick: ()-> Unit,
    modifier: Modifier = Modifier
) {
    val clickLabel = stringResource(
        if (isFavorite) R.string.unfavorite else R.string.favorite
    )
    CompositionLocalProvider(LocalContentAlpha provides  ContentAlpha.medium) {
        IconToggleButton(
            checked = isFavorite,
            onCheckedChange = { onClick() },
            modifier = modifier.semantics {
                this.onClick(label = clickLabel, action = null)
            }
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = null,
                tint = if (isFavorite) MaterialTheme.colors.error else Color.Gray
            )
        }
    }
}

@Composable
fun SpeakerAndTimeInterval(
    session: Session,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = session.speaker,
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = session.timeInterval,
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun SessionShortDescription(
    session: Session
) {
    Text(
        text = session.description.truncate(),
        //style = MaterialTheme.typography.body2,
        fontSize = 14.sp,
        fontWeight = FontWeight.Light
    )
}

@Preview("Favorite Button")
@Composable
fun FavoriteButtonPreview() {
    PodlodkaAndroidCrewTheme() {
        Surface {
            FavoriteButton(isFavorite = false, onClick = { })
            //SessionImage(Modifier.padding(8.dp))
        }
    }
}

@Preview("Favorite Button Favorites")
@Composable
fun FavoriteButtonFavoritesPreview() {
    PodlodkaAndroidCrewTheme {
        Surface {
            FavoriteButton(isFavorite = true, onClick = { })
        }
    }
}

@ExperimentalCoilApi
@Preview("Simple session card")
@Preview("Simple session card (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SimpleSessionPreview() {
    PodlodkaAndroidCrewTheme {
        Surface {
            val session = Session(
                id = "1",
                speaker = "Степан Чурюканов",
                date = "19 апреля",
                timeInterval = "10:00-11:00",
                description = "Доклад: Краткий экскурс в мир многопоточности",
                imageUrl = "https://static.tildacdn.com/tild3432-3435-4561-b136-663134643162/photo_2021-04-16_18-.jpg"
            )
            SessionCardSimple(session,{}, false, {})
        }
    }
}
