package com.ravimaurya.urjanext.presentation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ravimaurya.urjanext.presentation.components.AlertDialogUrja
import com.ravimaurya.urjanext.util.yeti
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(mainNavController: NavController) {

    val context = LocalContext.current

    val notifications = remember { generateSampleNotifications() }
    var selectedFilter by remember { mutableStateOf(NotificationFilter.All) }
    var showClearDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Notifications",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { mainNavController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showClearDialog = true }) {
                        Icon(Icons.Default.DeleteSweep, contentDescription = "Clear All")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Notification Filter Tabs
            FilterTabs(
                selectedFilter = selectedFilter,
                onFilterSelected = { selectedFilter = it }
            )

            // Unread Badge
            val unreadCount = notifications.count { it.isUnread }
            if (unreadCount > 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray.copy(alpha = 0.2f))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "$unreadCount unread notifications",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = {
                        /* Mark all as read */

                    }) {
                        Text("Mark all read")
                    }
                }
            }

            // Notifications List
            if (notifications.isEmpty()) {
                EmptyNotifications()
            } else {
                NotificationsList(
                    notifications = notifications.filter {
                        when (selectedFilter) {
                            NotificationFilter.All -> true
                            NotificationFilter.Charging -> it.type == NotificationType.CHARGING
                            NotificationFilter.Payments -> it.type == NotificationType.PAYMENT
                            NotificationFilter.System -> it.type == NotificationType.SYSTEM
                        }
                    }
                )
            }
        }

        // Clear All Dialog
        if (showClearDialog) {

            AlertDialogUrja(
                title = "Clear all notifications?",
                text = "This action cannot be undone.",
                confirmButtonText = "Clear All",
                onConfirmClick = { yeti(context) },
                onDismissClick = { showClearDialog = false }
            )

        }
    }
}

@Composable
fun FilterTabs(
    selectedFilter: NotificationFilter,
    onFilterSelected: (NotificationFilter) -> Unit,
) {
    val filters = NotificationFilter.entries

    ScrollableTabRow(
        selectedTabIndex = selectedFilter.ordinal,
        edgePadding = 16.dp,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        filters.forEachIndexed { index, filter ->
            Tab(
                selected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) },
                text = { Text(text = filter.title, fontWeight = FontWeight.Bold) }
            )
        }
    }

    Divider()
}

@Composable
fun NotificationsList(notifications: List<NotificationItem>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            DateHeader("Today")
        }

        val todayNotifications = notifications.filter { it.date.isToday() }
        items(todayNotifications) { notification ->
            NotificationCard(notification)
        }

        if (notifications.any { !it.date.isToday() }) {
            item {
                DateHeader("Earlier")
            }

            val earlierNotifications = notifications.filter { !it.date.isToday() }
            items(earlierNotifications) { notification ->
                NotificationCard(notification)
            }
        }
    }
}

@Composable
fun DateHeader(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NotificationCard(notification: NotificationItem) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isUnread)
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Notification icon
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(notification.type.backgroundColor.copy(alpha = 0.1f))
                    .border(
                        width = 1.dp,
                        color = notification.type.backgroundColor.copy(alpha = 0.3f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = notification.type.icon,
                    contentDescription = null,
                    tint = notification.type.backgroundColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Notification content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = notification.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = notification.message,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = if (expanded) Int.MAX_VALUE else 2,
                    overflow = TextOverflow.Ellipsis
                )

                AnimatedVisibility(
                    visible = expanded,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    if (notification.actionText != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                // View details of completed charging
                                yeti(context)
                            },
                            modifier = Modifier.align(Alignment.Start),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(notification.actionText)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = formatNotificationTime(notification.date),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }

            if (notification.isUnread) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
        }
    }
}

@Composable
fun EmptyNotifications() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "No notifications yet",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "We'll notify you about important updates and charging status",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                modifier = Modifier.padding(horizontal = 32.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

// Data models and helpers
enum class NotificationFilter(val title: String) {
    All("All"),
    Charging("Charging"),
    Payments("Payments"),
    System("System")
}

enum class NotificationType(
    val icon: ImageVector,
    val backgroundColor: Color,
) {
    CHARGING(Icons.Outlined.BatteryChargingFull, Color(0xFF4CAF50)),
    PAYMENT(Icons.Outlined.Payment, Color(0xFF2196F3)),
    SYSTEM(Icons.Outlined.Info, Color(0xFFFFA000))
}

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val type: NotificationType,
    val date: Date,
    val isUnread: Boolean,
    val actionText: String? = null,
)

fun formatNotificationTime(date: Date): String {
    val now = Calendar.getInstance()
    val notificationTime = Calendar.getInstance().apply { time = date }

    return when {
        now.get(Calendar.DAY_OF_YEAR) == notificationTime.get(Calendar.DAY_OF_YEAR) &&
                now.get(Calendar.YEAR) == notificationTime.get(Calendar.YEAR) -> {
            // Today
            SimpleDateFormat("h:mm a", Locale.getDefault()).format(date)
        }

        now.get(Calendar.DAY_OF_YEAR) - notificationTime.get(Calendar.DAY_OF_YEAR) == 1 &&
                now.get(Calendar.YEAR) == notificationTime.get(Calendar.YEAR) -> {
            // Yesterday
            "Yesterday, " + SimpleDateFormat("h:mm a", Locale.getDefault()).format(date)
        }

        now.get(Calendar.YEAR) == notificationTime.get(Calendar.YEAR) -> {
            // Same year
            SimpleDateFormat("MMM d, h:mm a", Locale.getDefault()).format(date)
        }

        else -> {
            // Different year
            SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(date)
        }
    }
}

fun Date.isToday(): Boolean {
    val now = Calendar.getInstance()
    val dateTime = Calendar.getInstance().apply { time = this@isToday }

    return now.get(Calendar.DAY_OF_YEAR) == dateTime.get(Calendar.DAY_OF_YEAR) &&
            now.get(Calendar.YEAR) == dateTime.get(Calendar.YEAR)
}

// Sample data generation
fun generateSampleNotifications(): List<NotificationItem> {
    val now = Date()
    val calendar = Calendar.getInstance()

    val notifications = mutableListOf<NotificationItem>()

    // Today's notifications
    notifications.add(
        NotificationItem(
            id = "1",
            title = "Charging Complete",
            message = "Your vehicle at Urja Power House has completed charging. 24 kWh delivered in 1h 15m.",
            type = NotificationType.CHARGING,
            date = now,
            isUnread = true,
            actionText = "View Details"
        )
    )

    calendar.add(Calendar.HOUR, -3)
    notifications.add(
        NotificationItem(
            id = "2",
            title = "Payment Successful",
            message = "Payment of ₹342 for charging session at Urja Power House was successful.",
            type = NotificationType.PAYMENT,
            date = calendar.time,
            isUnread = true
        )
    )

    calendar.add(Calendar.HOUR, -2)
    notifications.add(
        NotificationItem(
            id = "3",
            title = "Charging Started",
            message = "Charging has begun at Urja Power House. Estimated completion time is 1:30 PM.",
            type = NotificationType.CHARGING,
            date = calendar.time,
            isUnread = false
        )
    )

    // Yesterday's notifications
    calendar.add(Calendar.DAY_OF_YEAR, -1)
    notifications.add(
        NotificationItem(
            id = "4",
            title = "New Station Available",
            message = "A new charging station has been added near your frequent route. Check it out next time you need a charge!",
            type = NotificationType.SYSTEM,
            date = calendar.time,
            isUnread = false
        )
    )

    calendar.add(Calendar.HOUR, -5)
    notifications.add(
        NotificationItem(
            id = "5",
            title = "Maintenance Complete",
            message = "The scheduled maintenance at Greenway Station has been completed. All charging points are now operational.",
            type = NotificationType.SYSTEM,
            date = calendar.time,
            isUnread = false
        )
    )

    // Older notifications
    calendar.add(Calendar.DAY_OF_YEAR, -4)
    notifications.add(
        NotificationItem(
            id = "6",
            title = "May Invoice Available",
            message = "Your charging invoice for May 2025 is now available. Total amount: ₹2,140 for 12 charging sessions.",
            type = NotificationType.PAYMENT,
            date = calendar.time,
            isUnread = false,
            actionText = "View Invoice"
        )
    )

    return notifications
}

@Preview(showBackground = true)
@Composable
fun NotificationScreenPreview() {
    MaterialTheme {
        NotificationScreen(rememberNavController())
    }
}