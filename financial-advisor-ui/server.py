#!/usr/bin/env python3
"""
Simple HTTP server for Financial Advisor UI
"""

import http.server
import socketserver
import os
import sys
from pathlib import Path

# Configuration
PORT = 3001
DIRECTORY = Path(__file__).parent

class CORSHTTPRequestHandler(http.server.SimpleHTTPRequestHandler):
    """HTTP Request Handler with CORS support"""
    
    def end_headers(self):
        # Add CORS headers
        self.send_header('Access-Control-Allow-Origin', '*')
        self.send_header('Access-Control-Allow-Methods', 'GET, POST, OPTIONS')
        self.send_header('Access-Control-Allow-Headers', 'Content-Type')
        super().end_headers()
    
    def do_OPTIONS(self):
        """Handle preflight OPTIONS requests"""
        self.send_response(200)
        self.end_headers()
    
    def guess_type(self, path):
        """Override to set correct MIME types"""
        result = super().guess_type(path)
        
        # Handle different return formats
        if isinstance(result, tuple) and len(result) >= 2:
            mimetype, encoding = result[0], result[1]
        else:
            mimetype, encoding = result, None
        
        # Ensure correct MIME types
        if path.endswith('.js'):
            return 'application/javascript'
        elif path.endswith('.css'):
            return 'text/css'
        elif path.endswith('.html'):
            return 'text/html'
        
        return mimetype

def main():
    """Start the HTTP server"""
    # Change to the UI directory
    os.chdir(DIRECTORY)
    
    # Create server
    with socketserver.TCPServer(("", PORT), CORSHTTPRequestHandler) as httpd:
        print(f"🚀 Financial Advisor UI Server starting...")
        print(f"📁 Serving files from: {DIRECTORY}")
        print(f"🌐 Server running at: http://localhost:{PORT}")
        print(f"💰 Open your browser and visit: http://localhost:{PORT}")
        print(f"🔧 Backend should be running at: http://localhost:8080")
        print(f"\n🛑 Press Ctrl+C to stop the server\n")
        
        try:
            httpd.serve_forever()
        except KeyboardInterrupt:
            print(f"\n🛑 Server stopped by user")
            sys.exit(0)

if __name__ == "__main__":
    main()