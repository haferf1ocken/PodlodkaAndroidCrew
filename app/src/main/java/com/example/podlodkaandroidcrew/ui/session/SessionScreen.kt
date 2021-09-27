package com.example.podlodkaandroidcrew.ui.session

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import com.example.podlodkaandroidcrew.data.model.Session
import com.example.podlodkaandroidcrew.ui.theme.PodlodkaAndroidCrewTheme

@ExperimentalCoilApi
@Composable
fun SessionScreen(
    sessionId: String,
    viewModel: SessionViewModel
) {
    viewModel.getSessionById(sessionId)
    val session by remember(viewModel) { viewModel.session }.collectAsState()

    session?.let {
        if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            SessionContentLandscape(session = it)
        } else {
            SessionContentPortrait(session = it)
        }
    }
}

@ExperimentalCoilApi
@Composable
fun SessionContentPortrait(
    session: Session
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SessionContent(session = session)
    }
}

@ExperimentalCoilApi
@Composable
fun SessionContentLandscape(
    session: Session
) {
    Row(
        modifier = Modifier
            .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        SessionContent(
            session = session
        )
    }
}

@ExperimentalCoilApi
@Composable
fun SessionContent(
    session: Session,
    modifier: Modifier = Modifier
) {
    SessionImage(
        session = session,
        modifier = modifier
            .size(300.dp)
            .padding(16.dp)
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp)
    ) {
        SpeakerTitle(
            session = session,
            modifier = modifier.padding(bottom = 32.dp)
        )
        Column {
            SessionDateWithIcon(
                session = session,
                modifier = modifier.padding(bottom = 8.dp)
            )
            SessionFullDescription(
                session = session
            )
        }
    }
}

@Composable
fun SpeakerTitle(
    session: Session,
    modifier: Modifier
) {
    Text(
        text = session.speaker,
        style = MaterialTheme.typography.h5,
        modifier = modifier,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center
    )
}

@Composable
fun SessionDateWithIcon(
    session: Session,
    modifier: Modifier
) {
    Row {
        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = null,
            modifier = modifier
                .padding(end = 8.dp)
        )
        Text(
            text = "${session.date}, ${session.timeInterval}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Light
        )
    }
}

@Composable
fun SessionFullDescription(
    session: Session
) {
    Text(
        text = session.description,
        fontWeight = FontWeight.Light,
        fontSize = 18.sp
    )
}

@ExperimentalCoilApi
@Preview("Session screen")
@Preview("Session screen (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SessionScreenPortraitPreview() {
    PodlodkaAndroidCrewTheme {
        Surface {
            SessionContentPortrait(session = Session(
                id = "1",
                speaker = "Степан Чурюканов",
                date = "19 апреля",
                timeInterval = "10:00-11:00",
                description = "Доклад: Краткий экскурс в мир многопоточности",
                imageUrl = "https://static.tildacdn.com/tild3432-3435-4561-b136-663134643162/photo_2021-04-16_18-.jpg"
            ))
        }
    }
}

@ExperimentalCoilApi
@Preview(
    name = "Session landscape screen",
    device = Devices.PIXEL_C
)
@Preview(
    name = "Session landscape screen (dark)",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = Devices.PIXEL_C
)
@Composable
fun SessionScreenLandscapePreview() {
    PodlodkaAndroidCrewTheme {
        Surface {
            SessionContentLandscape(session = Session(
                id = "1",
                speaker = "Степан Чурюканов",
                date = "19 апреля",
                timeInterval = "10:00-11:00",
                description = "Доклад: Краткий экскурс в мир многопоточности",
                imageUrl = "https://static.tildacdn.com/tild3432-3435-4561-b136-663134643162/photo_2021-04-16_18-.jpg"
            ))
        }
    }
}