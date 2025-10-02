package com.easyestate.android.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.easyestate.android.AppDestination
import com.easyestate.android.R
import com.easyestate.android.ui.components.HomeNavigationBar
import com.easyestate.android.ui.theme.StitchInfo
import com.easyestate.android.ui.theme.EasyEstateTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertiesScreen(
    onBack: () -> Unit,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.landing_building_background),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Dimming Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f))
        )

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Properties",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            containerColor = Color.Transparent,
            bottomBar = {
                HomeNavigationBar(
                    onNavigate = onNavigate
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                item {
                    StatsChart()
                }
                item {
                    QuickActionsSection(onNavigate = onNavigate)
                }
            }
        }
    }
}

@Composable
private fun StatsChart() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            "Overview",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            StatCard(
                label = "Properties",
                value = "12",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                label = "Units",
                value = "60",
                color = StitchInfo,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                label,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                value,
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.ExtraBold),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun QuickActionsSection(
    onNavigate: (String) -> Unit
) {
    val actions = listOf(
        QuickAction("Add Property", Icons.Outlined.AddBusiness),
        QuickAction("Add Unit", Icons.Outlined.AddHome),
        QuickAction("Units", Icons.Outlined.Apartment),
        QuickAction("Meter", Icons.Outlined.Speed)
    )

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            "Quick Actions",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            maxItemsInEachRow = 2
        ) {
            actions.forEach { action ->
                PropertyActionTile(
                    action = action, onClick = {
                        when (action.title) {
                            "Add Property" -> onNavigate(AppDestination.AddProperty.route)
                            else -> { /* TODO: Handle other actions */ }
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun PropertyActionTile(
    action: QuickAction,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = action.icon,
            contentDescription = action.title,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(36.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = action.title,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            textAlign = TextAlign.Center
        )
    }
}

data class QuickAction(
    val title: String,
    val icon: ImageVector
)

@Preview(showBackground = true)
@Composable
private fun PropertiesScreenPreview() {
    EasyEstateTheme {
        PropertiesScreen(onBack = {}, onNavigate = { })
    }
}