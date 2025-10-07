package com.easyestate.android.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "Overview",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
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
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                label,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                value,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun QuickActionsSection(
    onNavigate: (String) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val actions = listOf(
        PropertyQuickAction(
            title = "Add Property",
            icon = Icons.Outlined.AddBusiness,
            route = AppDestination.AddProperty.route,
            accentColor = colorScheme.primary
        ),
        PropertyQuickAction(
            title = "Add Unit",
            icon = Icons.Outlined.AddHome,
            route = AppDestination.AddUnit.route,
            accentColor = colorScheme.primary
        ),
        PropertyQuickAction(
            title = "Edit Property",
            icon = Icons.Outlined.EditNote
        ),
        PropertyQuickAction(
            title = "View Units",
            icon = Icons.Outlined.Apartment
        ),
        PropertyQuickAction(
            title = "Rent Overview",
            icon = Icons.Outlined.AttachMoney
        ),
        PropertyQuickAction(
            title = "Meters",
            icon = Icons.Outlined.Speed
        ),
        PropertyQuickAction(
            title = "Tenants",
            icon = Icons.Outlined.Groups
        ),
        PropertyQuickAction(
            title = "Maintenance",
            icon = Icons.Outlined.Construction
        ),
        PropertyQuickAction(
            title = "Vacant / Occupied",
            icon = Icons.Outlined.HomeWork
        )
    )

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            "Quick Actions",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            actions.chunked(3).forEach { chunk ->
                val padded = chunk + List(3 - chunk.size) {
                    PropertyQuickAction(title = "", icon = null, route = null, accentColor = null, isPlaceholder = true)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    padded.forEach { action ->
                        PropertyActionTile(
                            action = action,
                            onClick = { action.route?.let(onNavigate) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RowScope.PropertyActionTile(
    action: PropertyQuickAction,
    onClick: () -> Unit
) {
    val isEnabled = action.route != null
    val colorScheme = MaterialTheme.colorScheme
    val accent = action.accentColor ?: colorScheme.primary
    val iconBackground = accent.copy(alpha = 0.1f)
    val iconTint = accent
    val labelColor = colorScheme.onSurface

    Column(
        modifier = Modifier
            .weight(1f)
            .clip(MaterialTheme.shapes.large)
            .let { base ->
                if (action.isPlaceholder) base.alpha(0f) else base.clickable(enabled = isEnabled, onClick = onClick)
            }
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(color = iconBackground, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            action.icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = if (action.isPlaceholder) null else action.title,
                    tint = iconTint,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (!action.isPlaceholder) {
            Text(
                text = action.title,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                textAlign = TextAlign.Center,
                color = labelColor
            )
        }
    }
}

data class PropertyQuickAction(
    val title: String,
    val icon: ImageVector?,
    val route: String? = null,
    val accentColor: Color? = null,
    val isPlaceholder: Boolean = false
)

@Preview(showBackground = true)
@Composable
private fun PropertiesScreenPreview() {
    EasyEstateTheme {
        PropertiesScreen(onBack = {}, onNavigate = { })
    }
}
