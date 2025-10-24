document.addEventListener('DOMContentLoaded', function() {
    const hamburger = document.getElementById('hamburger');
    const sidebar = document.getElementById('sidebar');
    const sidebarOverlay = document.getElementById('sidebarOverlay');
    const closeSidebar = document.getElementById('closeSidebar');

    // Function to open sidebar
    function openSidebar() {
        sidebar.classList.add('open');
        sidebarOverlay.classList.add('active');
        hamburger.classList.add('active');
        document.body.style.overflow = 'hidden'; // Prevent body scroll
    }

    // Function to close sidebar
    function closeSidebarFunc() {
        sidebar.classList.remove('open');
        sidebarOverlay.classList.remove('active');
        hamburger.classList.remove('active');
        document.body.style.overflow = ''; // Restore body scroll
    }

    // Event listeners
    if (hamburger) {
        hamburger.addEventListener('click', function(e) {
            e.preventDefault();
            if (sidebar.classList.contains('open')) {
                closeSidebarFunc();
            } else {
                openSidebar();
            }
        });
    }

    if (closeSidebar) {
        closeSidebar.addEventListener('click', closeSidebarFunc);
    }

    if (sidebarOverlay) {
        sidebarOverlay.addEventListener('click', closeSidebarFunc);
    }

    // Close sidebar when clicking on sidebar links (for better UX)
    const sidebarLinks = document.querySelectorAll('.sidebar-link');
    sidebarLinks.forEach(link => {
        link.addEventListener('click', function() {
            // Small delay to allow navigation to start
            setTimeout(closeSidebarFunc, 100);
        });
    });

    // Close sidebar on escape key
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape' && sidebar.classList.contains('open')) {
            closeSidebarFunc();
        }
    });

    // Handle window resize
    window.addEventListener('resize', function() {
        if (window.innerWidth > 768 && sidebar.classList.contains('open')) {
            closeSidebarFunc();
        }
    });
});
