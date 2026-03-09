<template>
  <div class="landing-page">
    <!-- 导航栏 -->
    <nav class="navbar">
      <div class="nav-brand">
        <span class="logo">🤖</span>
        <span class="brand-name">AI 代码助手</span>
      </div>
      <div class="nav-links">
        <a href="#features">功能特点</a>
        <a href="#scenarios">使用场景</a>
        <a href="#demo">快速体验</a>
      </div>
      <button class="nav-cta" @click="$emit('start-chat')">
        开始对话
      </button>
    </nav>

    <!-- Hero 区域 -->
    <section class="hero">
      <div class="hero-content">
        <div class="hero-badge">
          <span class="badge-icon">✨</span>
          <span>智能编程伙伴</span>
        </div>
        <h1 class="hero-title">
          让 AI 成为你的
          <span class="gradient-text">编程助手</span>
        </h1>
        <p class="hero-description">
          AI 代码助手是一款基于大语言模型的智能编程工具，帮助你快速解决编码问题、生成代码、优化架构，让开发效率翻倍提升。
        </p>
        <div class="hero-actions">
          <button class="btn-primary" @click="$emit('start-chat')">
            <span>立即体验</span>
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M5 12h14M12 5l7 7-7 7"/>
            </svg>
          </button>
          <button class="btn-secondary">
            <span>了解更多</span>
          </button>
        </div>
        <div class="hero-stats">
          <div class="stat-item">
            <span class="stat-number">10x</span>
            <span class="stat-label">开发效率提升</span>
          </div>
          <div class="stat-divider"></div>
          <div class="stat-item">
            <span class="stat-number">50+</span>
            <span class="stat-label">编程语言支持</span>
          </div>
          <div class="stat-divider"></div>
          <div class="stat-item">
            <span class="stat-number">24/7</span>
            <span class="stat-label">全天候服务</span>
          </div>
        </div>
      </div>
      <div class="hero-visual">
        <div class="chat-preview">
          <div class="preview-header">
            <div class="preview-dots">
              <span></span><span></span><span></span>
            </div>
            <span class="preview-title">AI 代码助手</span>
          </div>
          <div class="preview-content">
            <div class="preview-message user">
              <span>帮我写一个 Python 快速排序算法</span>
            </div>
            <div class="preview-message assistant">
              <div class="code-block">
                <span class="code-line"><span class="keyword">def</span> <span class="function">quick_sort</span>(arr):</span>
                <span class="code-line">  <span class="keyword">if</span> len(arr) <= <span class="number">1</span>:</span>
                <span class="code-line">    <span class="keyword">return</span> arr</span>
                <span class="code-line">  pivot = arr[<span class="number">0</span>]</span>
                <span class="code-line">  left = [x <span class="keyword">for</span> x <span class="keyword">in</span> arr[<span class="number">1</span>:]</span>
                <span class="code-line">    <span class="keyword">if</span> x < pivot]</span>
                <span class="code-line">  right = [x <span class="keyword">for</span> x <span class="keyword">in</span> arr[<span class="number">1</span>:]</span>
                <span class="code-line">    <span class="keyword">if</span> x >= pivot]</span>
                <span class="code-line">  <span class="keyword">return</span> quick_sort(left) + [pivot] + quick_sort(right)</span>
              </div>
            </div>
          </div>
        </div>
        <div class="floating-card card-1">
          <span class="card-icon">💡</span>
          <span>智能代码补全</span>
        </div>
        <div class="floating-card card-2">
          <span class="card-icon">🚀</span>
          <span>效率翻倍</span>
        </div>
        <div class="floating-card card-3">
          <span class="card-icon">🎯</span>
          <span>精准解答</span>
        </div>
      </div>
    </section>

    <!-- 功能特点 -->
    <section id="features" class="features">
      <div class="section-header">
        <span class="section-tag">功能特点</span>
        <h2 class="section-title">强大功能，助你高效开发</h2>
        <p class="section-desc">AI 代码助手集成了多种智能功能，全方位提升你的编程体验</p>
      </div>
      <div class="features-grid">
        <div class="feature-card" v-for="feature in features" :key="feature.title">
          <div class="feature-icon" :style="{ background: feature.gradient }">
            {{ feature.icon }}
          </div>
          <h3>{{ feature.title }}</h3>
          <p>{{ feature.description }}</p>
        </div>
      </div>
    </section>

    <!-- 使用场景 -->
    <section id="scenarios" class="scenarios">
      <div class="section-header">
        <span class="section-tag">使用场景</span>
        <h2 class="section-title">适用于各种开发场景</h2>
        <p class="section-desc">无论你是初学者还是资深开发者，AI 代码助手都能为你提供帮助</p>
      </div>
      <div class="scenarios-container">
        <div class="scenario-card" v-for="scenario in scenarios" :key="scenario.title">
          <div class="scenario-number">{{ scenario.number }}</div>
          <h3>{{ scenario.title }}</h3>
          <p>{{ scenario.description }}</p>
          <ul class="scenario-list">
            <li v-for="item in scenario.items" :key="item">{{ item }}</li>
          </ul>
        </div>
      </div>
    </section>

    <!-- CTA 区域 -->
    <section id="demo" class="cta-section">
      <div class="cta-content">
        <h2>准备好提升你的开发效率了吗？</h2>
        <p>立即开始与 AI 代码助手对话，体验智能编程的魅力</p>
        <button class="btn-primary btn-large" @click="$emit('start-chat')">
          开始对话
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M5 12h14M12 5l7 7-7 7"/>
          </svg>
        </button>
      </div>
      <div class="cta-decoration">
        <div class="deco-circle"></div>
        <div class="deco-circle"></div>
        <div class="deco-circle"></div>
      </div>
    </section>

    <!-- 页脚 -->
    <footer class="footer">
      <div class="footer-content">
        <div class="footer-brand">
          <span class="logo">🤖</span>
          <span>AI 代码助手</span>
        </div>
        <div class="footer-links">
          <a href="#">使用条款</a>
          <a href="#">隐私政策</a>
          <a href="#">帮助中心</a>
        </div>
        <div class="footer-copyright">
          © 2026 AI Code Helper. All rights reserved.
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const features = ref([
  {
    icon: '💬',
    title: '智能对话',
    description: '基于大语言模型，理解自然语言描述，精准解答编程问题',
    gradient: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
  },
  {
    icon: '⚡',
    title: '代码生成',
    description: '根据需求描述快速生成高质量代码，支持多种编程语言',
    gradient: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)'
  },
  {
    icon: '🔍',
    title: '代码审查',
    description: '智能分析代码质量，提供优化建议，帮助你写出更好的代码',
    gradient: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)'
  },
  {
    icon: '📚',
    title: '知识问答',
    description: '解答技术疑问，提供最佳实践，加速你的学习曲线',
    gradient: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)'
  },
  {
    icon: '🛠️',
    title: '问题诊断',
    description: '分析错误日志，定位问题根源，提供解决方案',
    gradient: 'linear-gradient(135deg, #fa709a 0%, #fee140 100%)'
  },
  {
    icon: '🔄',
    title: '代码重构',
    description: '优化代码结构，提升可读性和性能，遵循最佳编码规范',
    gradient: 'linear-gradient(135deg, #a18cd1 0%, #fbc2eb 100%)'
  }
])

const scenarios = ref([
  {
    number: '01',
    title: '学习编程',
    description: '适合编程初学者，快速入门各种编程语言和框架',
    items: ['语言基础讲解', '代码示例学习', '错误排查指导', '最佳实践建议']
  },
  {
    number: '02',
    title: '日常开发',
    description: '提升日常开发效率，解决编码过程中的各种问题',
    items: ['快速代码生成', 'API 使用指南', 'Bug 快速定位', '代码优化建议']
  },
  {
    number: '03',
    title: '项目架构',
    description: '提供架构设计建议，帮助你构建更健壮的系统',
    items: ['架构方案评估', '技术选型建议', '性能优化方案', '安全最佳实践']
  }
])
</script>

<style scoped>
.landing-page {
  min-height: 100vh;
  background: #fafafa;
  overflow-x: hidden;
}

/* 导航栏 */
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 48px;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
}

.nav-brand {
  display: flex;
  align-items: center;
  gap: 8px;
}

.logo {
  font-size: 24px;
}

.brand-name {
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
}

.nav-links {
  display: flex;
  gap: 32px;
}

.nav-links a {
  color: #6b7280;
  text-decoration: none;
  font-size: 14px;
  transition: color 0.2s;
}

.nav-links a:hover {
  color: #667eea;
}

.nav-cta {
  padding: 10px 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.nav-cta:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

/* Hero 区域 */
.hero {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 48px;
  align-items: center;
  max-width: 1200px;
  margin: 0 auto;
  padding: 120px 48px 80px;
  min-height: 100vh;
}

.hero-content {
  animation: fadeInUp 0.8s ease;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.hero-badge {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 6px 14px;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.1) 0%, rgba(118, 75, 162, 0.1) 100%);
  border-radius: 20px;
  font-size: 13px;
  color: #667eea;
  margin-bottom: 24px;
}

.badge-icon {
  font-size: 14px;
}

.hero-title {
  font-size: 48px;
  font-weight: 700;
  line-height: 1.2;
  color: #1f2937;
  margin-bottom: 24px;
}

.gradient-text {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-description {
  font-size: 18px;
  color: #6b7280;
  line-height: 1.8;
  margin-bottom: 32px;
}

.hero-actions {
  display: flex;
  gap: 16px;
  margin-bottom: 48px;
}

.btn-primary {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 14px 28px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(102, 126, 234, 0.4);
}

.btn-secondary {
  padding: 14px 28px;
  background: white;
  color: #1f2937;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-secondary:hover {
  border-color: #667eea;
  color: #667eea;
}

.btn-large {
  padding: 18px 36px;
  font-size: 18px;
}

.hero-stats {
  display: flex;
  align-items: center;
  gap: 32px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.stat-number {
  font-size: 28px;
  font-weight: 700;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.stat-label {
  font-size: 13px;
  color: #9ca3af;
}

.stat-divider {
  width: 1px;
  height: 40px;
  background: #e5e7eb;
}

/* Hero Visual */
.hero-visual {
  position: relative;
  animation: fadeInRight 0.8s ease 0.2s both;
}

@keyframes fadeInRight {
  from {
    opacity: 0;
    transform: translateX(20px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.chat-preview {
  background: #1e1e1e;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
}

.preview-header {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  background: #2d2d2d;
}

.preview-dots {
  display: flex;
  gap: 6px;
}

.preview-dots span {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background: #4d4d4d;
}

.preview-dots span:first-child { background: #ff5f56; }
.preview-dots span:nth-child(2) { background: #ffbd2e; }
.preview-dots span:last-child { background: #27c93f; }

.preview-title {
  font-size: 13px;
  color: #9ca3af;
}

.preview-content {
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.preview-message {
  padding: 12px 16px;
  border-radius: 12px;
  font-size: 14px;
}

.preview-message.user {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  align-self: flex-end;
  max-width: 80%;
}

.preview-message.assistant {
  background: #2d2d2d;
  color: #d4d4d4;
  max-width: 100%;
}

.code-block {
  display: flex;
  flex-direction: column;
  font-family: 'Consolas', 'Monaco', monospace;
  font-size: 12px;
}

.code-line {
  line-height: 1.8;
}

.keyword { color: #c586c0; }
.function { color: #dcdcaa; }
.number { color: #b5cea8; }

/* Floating Cards */
.floating-card {
  position: absolute;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 16px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  font-size: 14px;
  font-weight: 500;
  animation: float 3s ease-in-out infinite;
}

.card-icon {
  font-size: 18px;
}

.card-1 {
  top: -20px;
  right: 40px;
  animation-delay: 0s;
}

.card-2 {
  bottom: 60px;
  left: -40px;
  animation-delay: 1s;
}

.card-3 {
  bottom: -20px;
  right: 20px;
  animation-delay: 2s;
}

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}

/* Section 通用样式 */
.section-header {
  text-align: center;
  margin-bottom: 64px;
}

.section-tag {
  display: inline-block;
  padding: 6px 14px;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.1) 0%, rgba(118, 75, 162, 0.1) 100%);
  border-radius: 20px;
  font-size: 13px;
  color: #667eea;
  margin-bottom: 16px;
}

.section-title {
  font-size: 36px;
  font-weight: 700;
  color: #1f2937;
  margin-bottom: 16px;
}

.section-desc {
  font-size: 16px;
  color: #6b7280;
  max-width: 600px;
  margin: 0 auto;
}

/* 功能特点 */
.features {
  padding: 100px 48px;
  background: white;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 32px;
  max-width: 1200px;
  margin: 0 auto;
}

.feature-card {
  padding: 32px;
  background: #fafafa;
  border-radius: 16px;
  transition: all 0.3s;
}

.feature-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.08);
}

.feature-icon {
  width: 56px;
  height: 56px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 12px;
  font-size: 24px;
  margin-bottom: 20px;
}

.feature-card h3 {
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 12px;
}

.feature-card p {
  font-size: 14px;
  color: #6b7280;
  line-height: 1.6;
}

/* 使用场景 */
.scenarios {
  padding: 100px 48px;
  background: linear-gradient(180deg, #fafafa 0%, #f5f5f5 100%);
}

.scenarios-container {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 32px;
  max-width: 1200px;
  margin: 0 auto;
}

.scenario-card {
  padding: 32px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
  transition: all 0.3s;
}

.scenario-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.1);
}

.scenario-number {
  font-size: 48px;
  font-weight: 700;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-bottom: 16px;
}

.scenario-card h3 {
  font-size: 20px;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 12px;
}

.scenario-card > p {
  font-size: 14px;
  color: #6b7280;
  margin-bottom: 20px;
}

.scenario-list {
  list-style: none;
  padding: 0;
}

.scenario-list li {
  position: relative;
  padding-left: 20px;
  font-size: 14px;
  color: #4b5563;
  margin-bottom: 8px;
}

.scenario-list li::before {
  content: '✓';
  position: absolute;
  left: 0;
  color: #667eea;
}

/* CTA 区域 */
.cta-section {
  position: relative;
  padding: 100px 48px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  overflow: hidden;
}

.cta-content {
  text-align: center;
  position: relative;
  z-index: 1;
}

.cta-content h2 {
  font-size: 36px;
  font-weight: 700;
  color: white;
  margin-bottom: 16px;
}

.cta-content p {
  font-size: 18px;
  color: rgba(255, 255, 255, 0.9);
  margin-bottom: 32px;
}

.cta-decoration {
  position: absolute;
  inset: 0;
  pointer-events: none;
}

.deco-circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
}

.deco-circle:nth-child(1) {
  width: 300px;
  height: 300px;
  top: -150px;
  right: -100px;
}

.deco-circle:nth-child(2) {
  width: 200px;
  height: 200px;
  bottom: -100px;
  left: -50px;
}

.deco-circle:nth-child(3) {
  width: 100px;
  height: 100px;
  top: 50%;
  left: 20%;
}

/* 页脚 */
.footer {
  padding: 48px;
  background: #1f2937;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 24px;
}

.footer-brand {
  display: flex;
  align-items: center;
  gap: 8px;
  color: white;
  font-size: 18px;
  font-weight: 600;
}

.footer-links {
  display: flex;
  gap: 32px;
}

.footer-links a {
  color: #9ca3af;
  text-decoration: none;
  font-size: 14px;
  transition: color 0.2s;
}

.footer-links a:hover {
  color: white;
}

.footer-copyright {
  font-size: 13px;
  color: #6b7280;
}

/* 响应式 */
@media (max-width: 1024px) {
  .hero {
    grid-template-columns: 1fr;
    text-align: center;
    padding: 100px 24px 60px;
  }

  .hero-title {
    font-size: 36px;
  }

  .hero-actions {
    justify-content: center;
  }

  .hero-stats {
    justify-content: center;
  }

  .hero-visual {
    display: none;
  }

  .features-grid,
  .scenarios-container {
    grid-template-columns: 1fr;
  }

  .navbar {
    padding: 12px 24px;
  }

  .nav-links {
    display: none;
  }
}

@media (max-width: 640px) {
  .hero-title {
    font-size: 28px;
  }

  .section-title {
    font-size: 28px;
  }

  .hero-stats {
    flex-wrap: wrap;
    gap: 20px;
  }

  .stat-divider {
    display: none;
  }

  .features,
  .scenarios,
  .cta-section {
    padding: 60px 24px;
  }
}
</style>