package com.easyestate.android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.Construction
import androidx.compose.material.icons.outlined.GroupAdd
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.automirrored.outlined.NoteAdd
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.easyestate.android.ui.theme.StitchInfo
import com.easyestate.android.ui.theme.EasyEstateTheme

@Composable
fun HomeScreen(
    adminName: String,
    hasUnreadNotifications: Boolean = false,
    onLogout: () -> Unit,
    onAddTenantClick: () -> Unit,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            HomeHeader(
                adminName = adminName,
                hasUnreadNotifications = hasUnreadNotifications,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                onLogout = onLogout
            )
        },
        bottomBar = {
            HomeNavigationBar(
                onNavigate = onNavigate
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                OccupancyCard()
            }
            item {
                QuickActionsGrid(modifier, onAddTenantClick = onAddTenantClick)
            }
        }
    }
}

@Composable
private fun HomeHeader(
    adminName: String, hasUnreadNotifications: Boolean, onLogout: () -> Unit, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "Good Afternoon,",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = adminName,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            BadgedBox(badge = { if (hasUnreadNotifications) Badge() }) {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notifications", tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            IconButton(onClick = onLogout) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Logout,
                    contentDescription = "Logout",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun OccupancyCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val occupied = 45
            val vacant = 15
            val total = occupied + vacant

            OccupancyDonutChart(
                occupied = occupied,
                vacant = vacant,
                total = total,
                modifier = Modifier.size(100.dp)
            )

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OccupancyStat(
                    label = "Units Occupied",
                    value = occupied.toString(),
                    color = MaterialTheme.colorScheme.primary
                )
                OccupancyStat(
                    label = "Vacant",
                    value = vacant.toString(),
                    color = StitchInfo
                )
            }
        }
    }
}

@Composable
private fun OccupancyDonutChart(
    occupied: Int,
    vacant: Int,
    total: Int,
    modifier: Modifier = Modifier
) {
    val occupiedColor = MaterialTheme.colorScheme.primary
    val vacantColor = StitchInfo

    val occupiedAngle = 360f * occupied / total
    val vacantAngle = 360f * vacant / total

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(color = vacantColor, startAngle = -90f, sweepAngle = vacantAngle, useCenter = false, style = Stroke(width = 25f, cap = StrokeCap.Round))
            drawArc(color = occupiedColor, startAngle = -90f + vacantAngle, sweepAngle = occupiedAngle, useCenter = false, style = Stroke(width = 25f, cap = StrokeCap.Round))
        }
        Text(text = buildAnnotatedString {
            append(total.toString())
        }, style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
    }
}

@Composable
private fun OccupancyStat(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(modifier = Modifier
            .size(8.dp)
            .background(color = color, shape = MaterialTheme.shapes.small))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun UnitOverviewRow(
    units: List<UnitOverview>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        units.forEach { unit ->
            UnitOverviewCard(
                unit = unit,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun UnitOverviewCard(
    unit: UnitOverview,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = unit.label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = unit.value,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
            )
            unit.secondaryLabel?.let { secondary ->
                Text(
                    text = secondary,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

private data class UnitOverview(
    val label: String,
    val value: String,
    val secondaryLabel: String? = null,
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun QuickActionsGrid(modifier: Modifier = Modifier, onAddTenantClick: () -> Unit) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            maxItemsInEachRow = 3
        ) {
            DashboardQuickActions.forEach { action ->
                QuickActionTile(
                    action = action, onClick = {
                        when (action.title) {
                            "Add Tenant" -> onAddTenantClick()
                            // TODO: Handle other actions
                            else -> {}
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}


@Composable
private fun QuickActionTile(
    action: QuickAction,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    shape = MaterialTheme.shapes.extraLarge
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = action.icon,
                contentDescription = action.title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = action.title,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}


@Composable
private fun HomeNavigationBar(
    modifier: Modifier = Modifier,
    onNavigate: (String) -> Unit
) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = NavigationItems

    NavigationBar(
        modifier = modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        items.forEachIndexed { index, item ->
            val isSelected = selectedItem == index
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    selectedItem = index
                    onNavigate(item.route)
                },
                icon = { Icon(imageVector = item.icon, contentDescription = item.title, modifier = Modifier.size(30.dp)) },
                label = { Text(item.title, style = MaterialTheme.typography.bodySmall) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    }
}

private data class QuickAction(
    val title: String,
    val icon: ImageVector,
)

private data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

private val DashboardQuickActions = listOf(
    QuickAction("Send Bills", Icons.AutoMirrored.Outlined.ReceiptLong),
    QuickAction("Broadcast", Icons.Outlined.Campaign),
    QuickAction("Visitor Log", Icons.AutoMirrored.Outlined.ReceiptLong), // Using ReceiptLong as a substitute for Badge
    QuickAction("Notice", Icons.AutoMirrored.Outlined.NoteAdd), // Using NoteAdd as a substitute for sticky_note_2
    QuickAction("Payment", Icons.Outlined.Payments),
    QuickAction("Support", Icons.Outlined.SupportAgent),
    QuickAction("Properties", Icons.Outlined.Apartment),
    QuickAction("Maintenance", Icons.Outlined.Construction),
    QuickAction("Add Tenant", Icons.Outlined.GroupAdd)
)

private val NavigationItems = listOf(
    NavigationItem("Home", Icons.Outlined.Home, "home"),
    NavigationItem("Properties", Icons.Outlined.Apartment, "properties"),
    NavigationItem("Finances", Icons.Outlined.AccountBalanceWallet, "finances"),
    NavigationItem("Account", Icons.Outlined.AccountCircle, "account")
)

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    EasyEstateTheme {
        HomeScreen(adminName = "Easy Estate Admin", onLogout = {}, onAddTenantClick = {}, onNavigate = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreviewWithNotifications() {
    EasyEstateTheme {
        HomeScreen(adminName = "Philip", hasUnreadNotifications = true, onLogout = {}, onAddTenantClick = {}, onNavigate = {})
    }
}
