package com.example.podlodkaandroidcrew.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.podlodkaandroidcrew.data.model.Session
import com.example.podlodkaandroidcrew.extensions.truncate
import com.example.podlodkaandroidcrew.ui.theme.PodlodkaAndroidCrewTheme

@Composable
fun FavoritesSessionCard(
    session: Session,
    navigateToSession: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .requiredWidth(155.dp)
            .requiredHeight(150.dp)
            .clickable(onClick = { navigateToSession(session.id) })
        ,
        shape = RoundedCornerShape(20.dp),
        elevation = 10.dp
    ) {
        Column {
            SessionTimeAndDate(
                session = session,
                modifier = Modifier
                .padding(8.dp)
            )
            SpeakerAndDescription(
                session = session,
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
            )
        }
    }
}

@Composable
fun SessionTimeAndDate(
    session: Session,
    modifier: Modifier
) {
    Column(modifier = modifier) {
        Text(text = session.timeInterval, style = MaterialTheme.typography.h6)
        Text(text = session.date, fontWeight = FontWeight.Light)
    }
}

@Composable
fun SpeakerAndDescription(
    session: Session,
    modifier: Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = session.speaker.truncate(13),
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = session.description,
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.Light
        )
    }
}

@Preview("Favorites session card")
@Preview("Favorites session card (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FavoritesSessionCardPreview() {
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
            FavoritesSessionCard(session = session, navigateToSession = {})
        }
    }
}